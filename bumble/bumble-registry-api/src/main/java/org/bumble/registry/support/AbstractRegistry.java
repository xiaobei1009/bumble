package org.bumble.registry.support;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.Timer;
import java.util.TimerTask;
import java.util.TreeMap;

import org.bumble.base.BumbleNameBuilder;
import org.bumble.base.config.LocalConfigConst;
import org.bumble.base.config.LocalConfigHolder;
import org.bumble.base.model.Node;
import org.bumble.base.model.URL;
import org.bumble.registry.Registry;
import org.bumble.registry.RegistryChangedNotifier;
import org.bumble.registry.data.MngrNode;
import org.bumble.registry.data.RegistryData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractRegistry implements Registry {

	protected Logger logger = LoggerFactory.getLogger(this.getClass());
	
	private Boolean _needReload = false;
	
	protected Map<String, String> configCenterConfigMap = new HashMap<String, String>();
	
	protected List<RegistryChangedNotifier> watchers = new ArrayList<RegistryChangedNotifier>();
	
	private Timer timer = new Timer();
	
	public AbstractRegistry(Map<String, String> configCenterConfigMap) {
		this.configCenterConfigMap = configCenterConfigMap;
	}
	
	public void needReload(Boolean flag) {
		_needReload = flag;
	}
	
	public Boolean needReload() {
		return _needReload;
	}
	
	public void register() {
		try {
			InetAddress address = InetAddress.getLocalHost();
			String ip = address.getHostAddress();
			
			String uniqName = BumbleNameBuilder.getInstance().getUniqName();
			String port = LocalConfigHolder.getInstance().getConfig(LocalConfigConst.BumbleManagerServer.PORT);
			String url = new URL(ip, port).getUrl();
			
			register(uniqName, url);
		} catch (UnknownHostException e) {
			logger.error(e.getMessage());
			logger.trace(e.getMessage(), e);
		}
	}
	
	public void register(String name, String url) {
		doRegister(name, url);
	}

	public void unregister(String name) {
		doUnregister(name);
	}
	
	public MngrNode getMngr4Client() {
		MngrNode node = getMngr4Client(null);
		return node;
	}


	public MngrNode getMngr4Client(List<String> exceptNameList) {
		if (exceptNameList == null) {
			exceptNameList = new ArrayList<String>();
		}
		SortedMap<Integer, List<String>> result = new TreeMap<Integer, List<String>>();
		Map<String, MngrNode> mngrNodeMap = getData().getManagers();
		for (String mngrName : mngrNodeMap.keySet()) {
			MngrNode mngrNode = mngrNodeMap.get(mngrName);
			int clientCount = mngrNode.getClientNameList().size();
			List<String> mngrList = result.get(clientCount);
			if (mngrList == null) {
				mngrList = new ArrayList<String>();
			}
			mngrList.add(mngrName);
			result.put(clientCount, mngrList);
		}
		if (result.values().size() > 0) {
			List<String> mngrList = result.get(result.firstKey());
			Collections.sort(mngrList);
			String mngrName = null;
			Boolean targetFound = false;
			int i = 0;
			while (i < mngrList.size()) {
				mngrName = mngrList.get(i);
				if (exceptNameList.contains(mngrName)) {
					i++;
				} else {
					targetFound = true;
					break;
				}
			}
			if (targetFound) {
				return mngrNodeMap.get(mngrName);
			}
		}
		return null;
	}
	public void bindClientToManager(String mngrName) {
		try {
			String clientUniqName = BumbleNameBuilder.getInstance().getUniqName();
			InetAddress address = InetAddress.getLocalHost();
			String ip = address.getHostAddress();
			
			bindClientToManager(clientUniqName, ip, mngrName);
		} catch (UnknownHostException e) {
			logger.error(e.getMessage());
			logger.trace(e.getMessage(), e);
		}
	}
	
	public void bindClientToManager(String clientName, String clientUrl, String mngrName) {
		if (this.getData().getClientMngrMap().containsKey(clientName)) {
			String preMngrName = this.getData().getClientMngrMap().get(clientName);
			unbindClientFromManager(clientName, preMngrName);
		}
		doBindClientToManager(clientName, clientUrl, mngrName);
	}
	
	public void unbindClientFromManager(String clientName, String mngrName) {
		doUnbindClientFromManager(clientName, mngrName);
	}
	
	public void reload(RegistryData registryData) {
		for (String mngrName : registryData.getManagers().keySet()) {
			MngrNode mngrNode = registryData.getManagers().get(mngrName);
			doRegister(mngrName, mngrNode.getUrl());
			for (String clientName : mngrNode.getClients().keySet()) {
				Node clientNode = mngrNode.getClients().get(clientName);
				doBindClientToManager(clientName, clientNode.getUrl(), mngrName);
			}
		}
	}
	
	public void watchOnRegistry(RegistryChangedNotifier registryChangedNotifier) {
		this.watchers.add(registryChangedNotifier);
	}
	
	protected void notifyWatchers() {
		timer.cancel();
		timer = new Timer();
		
		// Wait a while for persistence process
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				RegistryData registryData = AbstractRegistry.this.getData();
				for (RegistryChangedNotifier watcher : watchers) {
					watcher.doNotify(registryData);
				}
			}
		}, 5000);
	}
	
	protected abstract void doRegister(String name, String url);
	protected abstract void doUnregister(String name);
	protected abstract void doBindClientToManager(String clientName, String clientUrl, String mngrName);
	protected abstract void doUnbindClientFromManager(String clientName, String mngrName);
}
