package org.bumble.core.remoting;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;

import org.bumble.base.BumbleNameBuilder;
import org.bumble.config.ConfigCenterConst;
import org.bumble.config.ConfigChangedNotifier;
import org.bumble.config.Configurator;
import org.bumble.config.ConfiguratorFactory;
import org.bumble.core.remoting.client.IRemotingTransporterClient;
import org.bumble.core.remoting.client.RemotingTransporterClientFactory;
import org.bumble.core.thread.ThreadExecutorGenerator;
import org.bumble.registry.Registry;
import org.bumble.registry.RegistryChangedNotifier;
import org.bumble.registry.RegistryFactory;
import org.bumble.registry.data.RegistryData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractRemotingTransporterFactory implements IRemotingTransporterFactory {

	protected Logger logger = LoggerFactory.getLogger(this.getClass());
	
	protected ExecutorService threadPool = null;
	
	protected String uniqName = null;
	protected List<String> closedClientNameList = new ArrayList<String>();
	
	public AbstractRemotingTransporterFactory() {
		this.threadPool = ThreadExecutorGenerator.getInstance().getExecutor();
		this.uniqName = BumbleNameBuilder.getInstance().getUniqName();
		watchOnConfig();
		watchOnRegistry();
	}
	
	protected Map<String, String> configCenterConfigMap = null;
	
	private Boolean healthCheckStarted = false;
	
	private void watchOnRegistry() {
		// Watch registry change before get registry data. Insure we can always get the new registry data
        RegistryFactory.getRegistry().watchOnRegistry(new RegistryChangedNotifier() {
			public void doNotify(RegistryData newData) {
				try {
					logger.debug("Registry is changed. Listener is notified.");
					doAfterRegistryChange(newData);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
        });
	}
	
	private void watchOnConfig() {
		Configurator configurator = ConfiguratorFactory.getConfigurator();
		configCenterConfigMap = configurator.getNamespacedConfig(new String[] {
			ConfigCenterConst.BumbleManagerServer.NS
		}, new ConfigChangedNotifier() {
			public void doNotify(String key, String value) {
				// Refresh the configuration
				configCenterConfigMap.put(key, value);
			}
		});
	}
	
	public void start() throws Exception {
		// Start
		doStart();
		
		// Health check
		healthCheck();
	}

	public void restart() throws Exception {
		// Do before Restart
		close();
		
		// Restart
		start();
	}

	public void close() {
		// Stop all the client socket
		for (String key : transporterClientMap.keySet()) {
			RemotingTransporterClientFactory.getInstance().closeClient(key);
		}
		transporterClientMap.clear();
		
		doClose();
	}
	
	protected abstract void doStart() throws Exception;
	protected abstract void doClose();
	protected Map<String, IRemotingTransporterClient> transporterClientMap = new ConcurrentHashMap<String, IRemotingTransporterClient>();
	
	public void closeTransporterClient(IRemotingTransporterClient client) {
		client.close();
		transporterClientMap.remove(client.getName());
	}
	
	protected abstract void doHandleUnhealthyTransporter(IRemotingTransporterClient transporter);
	
	protected void handleUnhealthyTransporter(IRemotingTransporterClient transporter) {
		// Handle unhealthy transporter outside
		doHandleUnhealthyTransporter(transporter);
		
		// Close the transporter socket connection
		closeTransporterClient(transporter);
		
		String name = transporter.getName();
		
		logger.info("Un-register bumble manager [" + name + "], as the client cannot connect to it any longer.");
		Registry registry = RegistryFactory.getRegistry();
		
		// Unregister
		registry.unregister(name);
	}
	
	protected abstract void doHealthCheck(Long heartBeatTimeout);
	
	private void healthCheck() {
		if (healthCheckStarted) {
			return;
		}
		healthCheckStarted = true;
		
		threadPool.execute(new Runnable() {
			
			public void run() {
				String healthCheckDelayStr = configCenterConfigMap.get(String.join(".", 
					ConfigCenterConst.ROOT, 
					ConfigCenterConst.BumbleManagerServer.NS, 
					ConfigCenterConst.BumbleManagerServer.HEALTH_CHECK_DELAY));
				int healthCheckDelay = healthCheckDelayStr == null ? 15 : Integer.valueOf(healthCheckDelayStr);
				
				String heartBeatTimeoutStr = configCenterConfigMap.get(String.join(".", 
					ConfigCenterConst.ROOT, 
					ConfigCenterConst.BumbleManagerServer.NS, 
					ConfigCenterConst.BumbleManagerServer.HEART_BEAT_TIMEOUT));
				Long heartBeatTimeout = heartBeatTimeoutStr == null ? 0 : Long.valueOf(heartBeatTimeoutStr);
					
				try {
					Thread.sleep(healthCheckDelay * 1000);
					
					while (true) {
						doHealthCheck(heartBeatTimeout);
						if (transporterClientMap != null) {
							for (String key : transporterClientMap.keySet()) {
								IRemotingTransporterClient client = transporterClientMap.get(key);
								if (client.isClosed()) {
									handleUnhealthyTransporter(client);
								} else if (!client.isConnected()) {
									continue;
								}
								Long lastAvailableTimestamp = client.getLastAvailableTimestamp();
								if (lastAvailableTimestamp == null) {
									Thread.sleep(5000);
									continue;
								}
								Long diffSec = (System.currentTimeMillis() - lastAvailableTimestamp) / 1000;
								if (diffSec > heartBeatTimeout) {
									logger.warn("Does not receive heart response from [" + key + "] for [" + diffSec + " Seconds]");
									handleUnhealthyTransporter(client);
								}
							}
						}
						Thread.sleep(12000);
					}
				} catch (Exception e) {
					logger.error(e.getMessage());
					logger.trace(e.getMessage(), e);
				}
			}
		});
	}
}
