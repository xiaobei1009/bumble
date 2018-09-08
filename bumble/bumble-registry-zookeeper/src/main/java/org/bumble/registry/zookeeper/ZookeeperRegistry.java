package org.bumble.registry.zookeeper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.I0Itec.zkclient.IZkChildListener;
import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.ZkConnection;
import org.bumble.base.model.Node;
import org.bumble.base.model.URL;

import org.bumble.config.ConfigCenterConst;
import org.bumble.registry.data.MngrNode;
import org.bumble.registry.data.RegistryData;
import org.bumble.registry.support.AbstractRegistry;

public class ZookeeperRegistry extends AbstractRegistry {
	
	private static final String ZK_PATH_MNGRS = "/managers";
	private static final String ZK_PATH_CLIENTS = "/clients";
	private static final String ZK_PATH_MNGRS_S = "/managers/";
	private static final String ZK_PATH_CLIENTS_S = "/clients/";
	
	private ZkClient zkClient = null;
	private Map<String, IZkChildListener> listeners = new HashMap<String, IZkChildListener>();
	
	public ZookeeperRegistry(Map<String, String> configCenterConfigMap) {
		super(configCenterConfigMap);
		makeNewZkClient();
	}
	
	private void makeNewZkClient() {
		String zkAddr = configCenterConfigMap.get(ConfigCenterConst.Registry.ADDRESS);
		int timeout = Integer.parseInt(configCenterConfigMap.get(ConfigCenterConst.Registry.TIMEOUT));
		
		zkClient = new ZkClient(new ZkConnection(zkAddr), timeout);
		
		zkClient.createPersistent(ZK_PATH_MNGRS, true);
		
		watchOnManagersNode();
	}
	
	private void watchOnManagersNode() {
		IZkChildListener listener = new IZkChildListener() {
			public void handleChildChange(String parentPath, List<String> currentChilds) throws Exception {
				notifyWatchers();
			}
		};
		zkClient.subscribeChildChanges(ZK_PATH_MNGRS, listener);
		listeners.put(ZK_PATH_MNGRS, listener);
		
		logger.debug("Watch on managers node.");
	}
	
	private void unWatch() {
		for (String path : listeners.keySet()) {
			IZkChildListener listener = listeners.get(path);
			zkClient.unsubscribeChildChanges(path, listener);
		}
	}
	
	@Override
	protected void doRegister(String name, String url) {
		String path = ZK_PATH_MNGRS_S + name;
		zkClient.createPersistent(path, true);
		zkClient.writeData(path, url);
		
		IZkChildListener listener = new IZkChildListener() {
			public void handleChildChange(String parentPath, List<String> currentChilds) throws Exception {
				notifyWatchers();
			}
		};
		zkClient.subscribeChildChanges(path + ZK_PATH_CLIENTS, listener);
		listeners.put(path + ZK_PATH_CLIENTS, listener);
	}
	
	@Override
	protected void doUnregister(String url) {
		String path = ZK_PATH_MNGRS_S + url;
		zkClient.deleteRecursive(path);
		
		IZkChildListener listener = listeners.get(path);
		zkClient.unsubscribeChildChanges(path, listener);
		listeners.remove(path);
	}
	
	@Override
	protected void doBindClientToManager(String clientName, String clientUrl, String mngrName) {
		String mngrPath = ZK_PATH_MNGRS_S + mngrName;
		if (zkClient.exists(mngrPath)) {
			String path = mngrPath + ZK_PATH_CLIENTS_S + clientName;
			zkClient.createPersistent(path, true);
			zkClient.writeData(path, clientUrl);
		}
	}
	
	@Override
	protected void doUnbindClientFromManager(String clientName, String mngrName) {
		zkClient.delete(ZK_PATH_MNGRS_S + mngrName + ZK_PATH_CLIENTS_S + clientName);
	}
	
	public RegistryData getData() {
		RegistryData rdObj = new RegistryData();
		try {
			List<String> mngrNames = zkClient.getChildren(ZK_PATH_MNGRS);
	        for (String mngrName : mngrNames) {
	        	String mngrPath = ZK_PATH_MNGRS_S + mngrName;
	        	String mngrUrl = zkClient.readData(mngrPath);
	        	MngrNode mngrNode = new MngrNode(mngrName, new URL(mngrUrl));
	        	String clientsPath = ZK_PATH_MNGRS_S + mngrName + ZK_PATH_CLIENTS;
	            if (zkClient.exists(clientsPath)) {
	            	List<String> clientNames = zkClient.getChildren(clientsPath);
	            	for (String clientName : clientNames) {
	            		String clientPath = ZK_PATH_MNGRS_S + mngrName + ZK_PATH_CLIENTS_S + clientName;
	            		String clientUrl = zkClient.readData(clientPath);
	            		mngrNode.addClient(new Node(clientName, new URL(clientUrl)));
	            	}
	            }
	            rdObj.addMngrNode(mngrNode);
	        }
		} catch (Exception e) {}
		return rdObj;
	}

	public void clear() {
		zkClient.deleteRecursive(ZK_PATH_MNGRS);
	}
	
	public void exit() {
		unWatch();
		zkClient.close();
	}
}
