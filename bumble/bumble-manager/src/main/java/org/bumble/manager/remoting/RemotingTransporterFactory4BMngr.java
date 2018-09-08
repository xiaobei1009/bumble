package org.bumble.manager.remoting;

import java.net.InetAddress;
import java.util.Iterator;
import java.util.Map;

import org.bumble.base.config.LocalConfigConst;
import org.bumble.base.config.LocalConfigHolder;
import org.bumble.base.model.Node;
import org.bumble.core.WorkNeedToShutdown;
import org.bumble.core.action.Action;
import org.bumble.core.remoting.AbstractRemotingTransporterFactory;
import org.bumble.core.remoting.client.IRemotingTransporterClient;
import org.bumble.core.remoting.client.RemotingTransporterClient;
import org.bumble.core.remoting.client.RemotingTransporterClientFactory;
import org.bumble.core.remoting.server.RemotingTransporterServer;

import org.bumble.registry.Registry;
import org.bumble.registry.RegistryFactory;
import org.bumble.registry.data.MngrNode;
import org.bumble.registry.data.RegistryData;

/**
 * Remoting Transporter Factory
 * Control center of the socket transport
 * 
 * @author shenxiangyu
 *
 */
public class RemotingTransporterFactory4BMngr extends AbstractRemotingTransporterFactory 
	implements IRemotingTransporterFactory4BMngr, WorkNeedToShutdown {

	private static RemotingTransporterFactory4BMngr instance = null;
	
	public static RemotingTransporterFactory4BMngr getInstance() {
		if (instance == null) {
			instance = new RemotingTransporterFactory4BMngr();
		}
		return instance;
	}
	
	public RemotingTransporterFactory4BMngr() {
		super();
	}
	
	private RemotingTransporterServer transporterServer = null;
	
	@Override
	protected void doClose() {
		// Stop the server socket
		if (transporterServer != null) {
			transporterServer.close();
		}
	}
	
	@Override
	public void doStart() throws Exception {
		// Start the server socket
		startServer();
		
		registryData = RegistryFactory.getRegistry().getData();
		
		// Connect to the brother bumble managers via socket connection
		connectToBrothers(registryData);
	}
	
	public String getUniqName() {
		return this.uniqName;
	}
	
	private String url = null;
	private RegistryData registryData = null;
	
	private void startServer() throws Exception {
		Map<String, String> props = LocalConfigHolder.getInstance().getConfigForNamespace(LocalConfigConst.BumbleManagerServer.NS);
		int port = Integer.valueOf(props.get(LocalConfigConst.BumbleManagerServer.PORT));
		
		InetAddress address = InetAddress.getLocalHost();
		String ip = address.getHostAddress();
		
		url = ip + ":" + port;
		
		transporterServer = new RemotingTransporterServer(threadPool, uniqName, port);
		transporterServer.start();
	}
	
	/**
	 * Connect to the brother bumble managers
	 * @param registryData
	 * @throws Exception
	 */
	private void connectToBrothers(RegistryData registryData) throws Exception {
		
		logger.debug("Try to connect to brothers.");
		logger.debug("Current manager list: " + registryData.getManagerNameList().toString());
		for (Iterator<String> it = closedClientNameList.iterator(); it.hasNext();) {
			String closedClientName = it.next();
			if (!registryData.getManagerNameList().contains(closedClientName)) {
				it.remove();
			}
		}
		
		for (String name : registryData.getManagers().keySet()) {
			if (name.equals(this.uniqName)) {
				logger.debug("[" + name + "] is me, skip.");
				continue;
			}
			if (transporterClientMap.containsKey(name)) {
				logger.debug("[" + name + "] already exists, skip.");
				continue;
			}
			if (closedClientNameList.contains(name)) {
				logger.debug("[" + name + "] is down, skip");
				continue;
			}
			MngrNode mngrNode = registryData.getManagers().get(name);
			if (mngrNode.getUrl().equals(this.url)) {
				logger.debug("[" + name + "] is a previous instance of me(with same server ip port), unregister and skip.");
				RegistryFactory.getRegistry().unregister(name);
				continue;
			}
			
			logger.info("Connecting to [" + name + "]");
			IRemotingTransporterClient client = (RemotingTransporterClient) transporterClientMap.get(name);
			if (client == null || client.isClosed()) {
				client = RemotingTransporterClientFactory.getInstance().getClient(name, mngrNode.getIp(), Integer.valueOf(mngrNode.getPort()));
				
				client.start();
				if (client.isClosed()) {
					handleUnhealthyTransporter(client);
					closedClientNameList.add(client.getName());
				} else {
					transporterClientMap.put(name, client);
				}
			}
		}
	}
	
	private void closeRedundantClient(RegistryData registryData) {
		for (String name : transporterClientMap.keySet()) {
			if (!registryData.getManagerNameList().contains(name)) {
				IRemotingTransporterClient client = transporterClientMap.get(name);
				logger.debug("[" + name + "] is out of date, and not living in the registry, so close this socket client.");
				
				RemotingTransporterClientFactory.getInstance().closeClient(client);
				transporterClientMap.remove(name);
			}
		}
	}
	
	@Override
	protected void doHandleUnhealthyTransporter(IRemotingTransporterClient transporter) {}

	@Override
	protected void doHealthCheck(Long heartBeatTimeout) {
		Registry registry = RegistryFactory.getRegistry();
		Long currTimestamp = System.currentTimeMillis();
		Map<String, Node> clients = transporterServer.getClientsConnectedToServer();
		for (String name : clients.keySet()) {
			Node client = clients.get(name);
			Long diffSec = (currTimestamp - client.getLastAvailableTimestamp()) / 1000;
			if (diffSec < heartBeatTimeout) {
				continue;
			}
			
			logger.warn("Client [" + name + "] has not touch this server for[" + diffSec + " Seconds]");
			logger.warn("Try to un-bind [" + name + "] from [" + this.uniqName + "] if it is a bumble client");
			registry.unbindClientFromManager(name, this.uniqName);
			
			logger.warn("Try to unregister [" + name + "] if it is a bumble manager");
			registry.unregister(name);
			clients.remove(name);
		}
	}
	
	public void doAfterRegistryChange(RegistryData registryData) {
		try {
			// Connect to brothers as there will be new bumble manager registered
			connectToBrothers(registryData);
			
			this.registryData = registryData;
			
			// Close redundant client as there will be bumble managers with a new name living in the registry
			// And the client according to the name which is out of date should be closed 
			closeRedundantClient(registryData);
		} catch (Exception e) {
			logger.error(e.getMessage());
			logger.trace(e.getMessage(), e);
		}
	}

	public void shutdown() {
		// Un-register
		logger.info("Un-register [" + this.uniqName + "] before exit.");
		Registry registry = RegistryFactory.getRegistry();
		registry.unregister(this.uniqName);
		
		// Close socket
		close();
	}

	public void sendMsgToMngr(String mngrName, Action action) {
		IRemotingTransporterClient client = transporterClientMap.get(mngrName);
		if (client != null) {
			client.sendMsg(action.toJsonString());
		}
	}
	
	public void sendMsgToClient(String clientName, Action action) {
		transporterServer.sendMsgToClient(clientName, action.toJsonString());
	}
	
	public void sendMsg(String uniqName, Action action) {
		sendMsgToMngr(uniqName, action);
		sendMsgToClient(uniqName, action);
	}
}
