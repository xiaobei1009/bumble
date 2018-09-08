package org.bumble.manager.txn;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;

import org.bumble.base.config.LocalConfigConst;
import org.bumble.base.config.LocalConfigHolder;
import org.bumble.config.ConfigCenterConst;
import org.bumble.config.ConfigChangedNotifier;
import org.bumble.config.Configurator;
import org.bumble.config.ConfiguratorFactory;
import org.bumble.core.BumbleConst;
import org.bumble.core.action.Action;
import org.bumble.core.action.ActionConst;
import org.bumble.core.thread.ThreadExecutorGenerator;
import org.bumble.manager.remoting.RemotingTransporterFactory4BMngr;
import org.bumble.registry.Registry;
import org.bumble.registry.RegistryChangedNotifier;
import org.bumble.registry.RegistryFactory;
import org.bumble.registry.data.RegistryData;
import org.bumble.store.IStoreService;
import org.bumble.store.StoreServiceFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TxnGroupFactory {
	
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	private static TxnGroupFactory instance = new TxnGroupFactory();
	private RegistryData registryData = null;
	
	private ExecutorService threadPool = ThreadExecutorGenerator.getInstance().getExecutor();
	
	private RemotingTransporterFactory4BMngr transporterFactory = null;
	
	private int tranEndCheckTimeout = 10000;
	
	private IStoreService storeService = null;
	
	public static TxnGroupFactory getInstance() {
		return instance;
	}
	public TxnGroupFactory() {
		final TxnGroupFactory _this = this;
		Registry registry = RegistryFactory.getRegistry();
		registryData = registry.getData();
		registry.watchOnRegistry(new RegistryChangedNotifier() {
			public void doNotify(RegistryData registryData) {
				logger.debug("Registry is changed, update registry data");
				instance.setRegistryData(registryData);
			}
		});
		
		Configurator configurator = ConfiguratorFactory.getConfigurator();
		String tranEndCheckTimeoutStr = configurator.getConfig(new String[] {
			ConfigCenterConst.BumbleManagerServer.NS,
			ConfigCenterConst.BumbleManagerServer.TRAN_END_CHECK_TIMEOUT
		}, new ConfigChangedNotifier() {
			public void doNotify(String key, String value) {
				_this.tranEndCheckTimeout = Integer.valueOf(value);
				_this.updateTranEndCheckTimeoutForAll();
			}
		});
		this.tranEndCheckTimeout = Integer.valueOf(tranEndCheckTimeoutStr);
		
		transporterFactory = RemotingTransporterFactory4BMngr.getInstance();
		
		storeService = StoreServiceFactory.getStoreService();
	}
	
	public void updateTranEndCheckTimeoutForAll() {
		Iterator<String> iterator = txnGroups.keySet().iterator();
		while (iterator.hasNext()) {
			TxnGroup txnGroup = txnGroups.get(iterator.next());
			txnGroup.setTranEndCheckTimeout(tranEndCheckTimeout);
		}
	}
	
	private Map<String, TxnGroup> txnGroups = new ConcurrentHashMap<String, TxnGroup>();
	
	public TxnGroup newTxnGroup(String txnGroupId, Txn txn) {
		TxnGroup txnGroup = new TxnGroup(txnGroupId, txn);
		txnGroup.setTranEndCheckTimeout(tranEndCheckTimeout);
		
		txnGroups.put(txnGroupId, txnGroup);
		
		storeService.set(txnGroupId, txnGroup.toJsonString());
		
		return txnGroup;
	}
	
	public void addUpdateTxn(String txnGroupId, Txn txn, Action action) {
		TxnGroup txnGroup = txnGroups.get(txnGroupId);
		if (txnGroup != null) {
			logger.debug("Update transaction in the group");
			txnGroup.addUpdateTxn(txn);
			
			storeService.set(txnGroupId, txnGroup.toJsonString());
		}
	}
	
	/**
	 * <pre>
	 * Finish a Transaction Group
	 * 
	 * Check to see if all the transaction participants are ready
	 * And decide to send commit/rollback message to all the
	 * transaction participants
	 * </pre>
	 * @param txnGroupId
	 */
	public void finish(final String txnGroupId) {
		threadPool.execute(new Runnable() {
			public void run() {
				TxnGroup txnGroup = txnGroups.get(txnGroupId);
				if (txnGroup != null) {
					txnGroup.stopGrowing();
					
					Boolean isNotReady = false;
					Boolean isFailed = false;
					for (String txnId : txnGroup.getTxns().keySet()) {
						Txn txn = txnGroup.getTxns().get(txnId);
						if (txn.getState().equals(Txn.State.INIT)) {
							isNotReady = true;
							break;
						}
						if (!txn.getState().equals(Txn.State.SUCCESS)) {
							isFailed = true;
							break;
						}
					}
					
					if (isNotReady) {
						logger.warn("The transaction group [" + txnGroupId + "] is not ready");
						if (txnGroup.isWaitingParticipantsStateTimeout()) {
							isFailed = true;
						} else {
							txnGroup.startFailCountDown();
							return;
						}
					}
					
					Action action = null;
					String actionType = null;
					if (isFailed) {
						// Send roll back message to all the participants
						actionType = ActionConst.Type.ROLLBACK;
						
						logger.debug("Ready notify all the clients to rollback the whole transaction group");
					} else {
						// Send commit message to all the participants
						actionType = ActionConst.Type.COMMIT;
						
						logger.debug("Ready notify all the clients to commit the whole transaction group");
					}
					action = new Action(actionType);
					action.setParamEntry(BumbleConst.TXN_GROUP_ID, txnGroupId);
					
					String thisMngrUniqName = LocalConfigHolder.getInstance().getConfig(LocalConfigConst.Basic.UNIQ_NAME);
					
					for (String txnId : txnGroup.getTxns().keySet()) {
						Txn txn = txnGroup.getTxns().get(txnId);
						String clientUniqName = txn.getClientUniqName();
						String mngrUniqName = getMngrUnqName4Client(clientUniqName);
						
						action.setParamEntry(BumbleConst.TXN_ID, txnId);
						action.setParamEntry(BumbleConst.RESP_CONDITION_ID, txnId);
						action.setParamEntry(BumbleConst.CLIENT_UNIQ_NAME, clientUniqName);
						
						if (mngrUniqName == null) {
							logger.warn("Client [" + clientUniqName + "] has no manager");
						} else if (mngrUniqName.equals(thisMngrUniqName)) {
							logger.debug("Send message directly to the client [" + clientUniqName + "]");
							transporterFactory.sendMsgToClient(clientUniqName, action);
						} else {
							logger.debug("Dispatch message to the right manager [" + mngrUniqName + "] for client [" + clientUniqName + "]");
							transporterFactory.sendMsgToMngr(mngrUniqName, action);
						}
					}
				}
			}
		});
	}
	
	private String getMngrUnqName4Client(String clientUniqName) {
		String mngrUniqName = registryData.getClientMngrMap().get(clientUniqName);
		if (mngrUniqName == null) {
			registryData = RegistryFactory.getRegistry().getData();
			mngrUniqName = registryData.getClientMngrMap().get(clientUniqName);
		}
		return mngrUniqName;
	}
	
	public RegistryData getRegistryData() {
		return registryData;
	}

	public void setRegistryData(RegistryData registryData) {
		this.registryData = registryData;
	}
	
	public TxnGroup getGroup(String txnGroupId) {
		return this.txnGroups.get(txnGroupId);
	}
}
