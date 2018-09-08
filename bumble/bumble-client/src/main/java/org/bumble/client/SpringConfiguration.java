package org.bumble.client;

import org.bumble.base.BasicConst;
import org.bumble.base.config.LocalConfigConst;
import org.bumble.base.config.LocalConfigHolder;
import org.bumble.client.remoting.RemotingTransporterFactory4BClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import org.bumble.config.ConfigCenterConst;
import org.bumble.config.ConfigChangedNotifier;
import org.bumble.config.Configurator;
import org.bumble.config.ConfiguratorFactory;

@Configuration
@ComponentScan
public class SpringConfiguration {
	
	private static Logger logger = LoggerFactory.getLogger(SpringConfiguration.class);
	static {
		logger.info("Spring boot scanning bumble client transaction configuration...");
		
		Thread t = new Thread(new Runnable() {

			public void run() {
				try {
					final RemotingTransporterFactory4BClient client = RemotingTransporterFactory4BClient.getInstance();
					
					String name = LocalConfigHolder.getInstance().getConfig(LocalConfigConst.Basic.NAME);
					
					Configurator configurator = ConfiguratorFactory.getConfigurator();
					String bumbleEnabledStr = configurator.getConfig(new String[] {
				    		ConfigCenterConst.BumbleClient.NS,
				    		ConfigCenterConst.BumbleClient.ENABLE_BUMBLE,
				    		name
				    	}, new ConfigChangedNotifier() {
						public void doNotify(String key, String value) {
							logger.debug("Bumble enable changed to: " + value);
							try {
								if (value.equals(BasicConst.YES) && !client.isConnected()) {
									client.start();
								}
								if (value.equals(BasicConst.NO) && client.isConnected()) {
									client.close();
								}
							} catch (Exception e) {
								logger.error(e.getMessage());
								logger.trace(e.getMessage(), e);
							}
						}
					});
					
					if (bumbleEnabledStr.equals(BasicConst.YES)) {
						client.start();
					}
				} catch (Exception e) {
					logger.error(e.getMessage());
					logger.trace(e.getMessage(), e);
				}
			}
			
		});
		t.start();
	}
}
