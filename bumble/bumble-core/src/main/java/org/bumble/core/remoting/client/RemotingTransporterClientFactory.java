package org.bumble.core.remoting.client;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;

import org.bumble.config.ConfigCenterConst;
import org.bumble.config.ConfigChangedNotifier;
import org.bumble.config.Configurator;
import org.bumble.config.ConfiguratorFactory;
import org.bumble.core.thread.ThreadExecutorGenerator;

public class RemotingTransporterClientFactory {
	
	private volatile static RemotingTransporterClientFactory instance = null;
	public static RemotingTransporterClientFactory getInstance() {
		if (instance == null) {
			instance = new RemotingTransporterClientFactory();
		}
		return instance;
	}
	
	private volatile int heartBeatInterval = 0;
	private volatile int socketConnectRetryInterval = 0;
	
	public RemotingTransporterClientFactory() {
		final RemotingTransporterClientFactory _this = this;
		
		threadPool = ThreadExecutorGenerator.getInstance().getExecutor();
		
		Configurator configurator = ConfiguratorFactory.getConfigurator();
		String heartBeatIntervalStr = configurator.getConfig(new String[] {
			ConfigCenterConst.BumbleCore.NS,
			ConfigCenterConst.BumbleCore.HEART_BEAT_INTERVAL
		}, new ConfigChangedNotifier() {
			public void doNotify(String key, String value) {
				_this.setHeartBeatInterval(Integer.valueOf(value));
			}
		});
		_this.setHeartBeatInterval(Integer.valueOf(heartBeatIntervalStr));
		
		String socketConnectRetryIntervalStr = configurator.getConfig(new String[] {
			ConfigCenterConst.BumbleCore.NS,
			ConfigCenterConst.BumbleCore.SOCKET_CONNECT_RETRY_INTERVAL
		}, new ConfigChangedNotifier() {
			public void doNotify(String key, String value) {
				_this.setSocketConnectRetryInterval(Integer.valueOf(value));
			}
		});
		_this.setSocketConnectRetryInterval(Integer.valueOf(socketConnectRetryIntervalStr));
	}
	
	private ExecutorService threadPool = null;
	
	private Map<String, IRemotingTransporterClient> clientMap = new HashMap<String, IRemotingTransporterClient>();
	
	public IRemotingTransporterClient getClient(String name, String ip, int port) {
		IRemotingTransporterClient client = null;
		if (clientMap.containsKey(name)) {
			client = clientMap.get(name);
		} else {
			client = new RemotingTransporterClient(threadPool, name, ip, port);
		}
		return client;
	}
	
	public void closeClient(String name) {
		IRemotingTransporterClient client = clientMap.get(name);
		closeClient(client);
	}
	
	public void closeClient(IRemotingTransporterClient client) {
		client.close();
		clientMap.remove(client.getName());
	}

	public int getHeartBeatInterval() {
		return heartBeatInterval;
	}

	public void setHeartBeatInterval(int heartBeatInterval) {
		this.heartBeatInterval = heartBeatInterval;
	}

	public int getSocketConnectRetryInterval() {
		return socketConnectRetryInterval;
	}

	public void setSocketConnectRetryInterval(int socketConnectRetryInterval) {
		this.socketConnectRetryInterval = socketConnectRetryInterval;
	}
}
