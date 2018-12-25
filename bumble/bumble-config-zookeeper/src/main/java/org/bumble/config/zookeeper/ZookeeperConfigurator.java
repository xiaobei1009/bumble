package org.bumble.config.zookeeper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.I0Itec.zkclient.IZkDataListener;
import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.ZkConnection;
import org.bumble.base.BasicConst;
import org.bumble.base.config.LocalConfigConst;
import org.bumble.base.config.LocalConfigHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;
import org.bumble.config.AbstractConfigurator;
import org.bumble.config.ConfigChangedNotifier;

public class ZookeeperConfigurator extends AbstractConfigurator {

	Logger logger = LoggerFactory.getLogger(this.getClass());
	
	private ZkClient zkClient = null;
	
	private static final String CONFIG_ROOT = "config";
	private static final String CONFIG_ROOT_PATH = BasicConst.SLASH + "config";
	
	public ZookeeperConfigurator() {
		Map<String, String> props = LocalConfigHolder.getInstance().getConfigForNamespace(LocalConfigConst.ConfigCenter.NS);
		String zkAddr = props.get(LocalConfigConst.ConfigCenter.ADDRESS);
		String timeoutStr = props.get(LocalConfigConst.ConfigCenter.TIMEOUT);
		
		int timeout = Integer.parseInt(timeoutStr);
		
		zkClient = new ZkClient(new ZkConnection(zkAddr), timeout);
	}
	
	private String keyChainToPath(String[] keyChain) {
		String path = CONFIG_ROOT_PATH;
		for (String key : keyChain) {
			path += BasicConst.SLASH +  key;
		}
		return path;
	}
	
	public void setConfig(String[] keyChain, String value) {
		String path = keyChainToPath(keyChain);
		
		if (!zkClient.exists(path)) {
			zkClient.createPersistent(path, true);
		}
		
		zkClient.writeData(path, value);
	}
	
	public boolean configExists(String[] keyChain) {
		String path = keyChainToPath(keyChain);
		return zkClient.exists(path);
	}
	
	public String getConfig(String[] keyChain) {
		String path = keyChainToPath(keyChain);
		if (!zkClient.exists(path)) {
			return null;
		}
		String value = zkClient.readData(path);
		return value;
	}
	
	private void setConfigForJson(JSONObject jsnObj, List<String> keyChain) {
		if (keyChain == null) {
			keyChain = new ArrayList<String>();
		}
		for (String key : jsnObj.keySet()) {
			
			JSONObject subJsnObj = jsnObj.getJSONObject(key);
			
			if (subJsnObj.keySet().isEmpty()) {
				String value = subJsnObj.toString();
				setConfig(keyChain.toArray(new String[keyChain.size()]), value);
			} else {
				if (!(keyChain.isEmpty() && key.equals(CONFIG_ROOT))) {
					keyChain.add(key);
				}
				setConfigForJson(subJsnObj, keyChain);
			}
		}
	}
	
	public void setAllConfig(String json) {
		JSONObject jsnObj = (JSONObject)JSONObject.parse(json);
		setConfigForJson(jsnObj, null);
	}

	public String getAllConfig() {
		String ret = getAllConfig(null);
		return ret;
	}
	
	public String getAllConfig(ConfigChangedNotifier notifier) {
		JSONObject jsnObj = getSubsequentConfigAndWatch(CONFIG_ROOT_PATH, notifier);
		String ret = jsnObj.toJSONString();
		return ret;
	}
	
	public String getSubsequentConfigAndWatch(String[] keyChain, ConfigChangedNotifier notifier) {
		String path = keyChainToPath(keyChain);
		JSONObject jsnObj = getSubsequentConfigAndWatch(path, notifier);
		String ret = jsnObj.toJSONString();
		return ret;
	}

	public void shutdown() {
		zkClient.close();
	}

	@SuppressWarnings("unchecked")
	private <T extends Object> T getSubsequentConfigAndWatch(String path, final ConfigChangedNotifier notifier) {
		Object jsnObj = new JSONObject();
		List<String> keys = zkClient.getChildren(path);
		if (keys.isEmpty()) {
			String value = zkClient.readData(path);
			
			if (notifier != null) {
				watchOnConfig(path, notifier);
			}
			jsnObj = value;
		} else {
			for (String key : keys) {
				Object subJsnObj = getSubsequentConfigAndWatch(path + BasicConst.SLASH + key, notifier);
				((JSONObject)jsnObj).put(key, subJsnObj);
			}
		}
		return (T) jsnObj;
	}
	
	public void clear() {
		if (zkClient.exists(CONFIG_ROOT_PATH)) {
			zkClient.deleteRecursive(CONFIG_ROOT_PATH);
		}
	}

	private void watchOnConfig(String path, final ConfigChangedNotifier notifier) {
		zkClient.subscribeDataChanges(path, new IZkDataListener() {
			public void handleDataChange(String dataPath, Object data) throws Exception {
				logger.info("[zk config handleDataChange]: [" + dataPath + ": " + data + "]");
				
				notifier.doNotify(dataPath, (String)data);
			}
			public void handleDataDeleted(String dataPath) throws Exception {
				logger.warn("data deleted: " + dataPath);
			}
        });
	}
	
	public void watchOnConfig(String[] keyChain, ConfigChangedNotifier notifier) {
		String path = keyChainToPath(keyChain);
		watchOnConfig(path, notifier);
	}

	public String getConfig(String[] keyChain, ConfigChangedNotifier notifier) {
		String value = getConfig(keyChain);
		watchOnConfig(keyChain, notifier);
		return value;
	}
}
