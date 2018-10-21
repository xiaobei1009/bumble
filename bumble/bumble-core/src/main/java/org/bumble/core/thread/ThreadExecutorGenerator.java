package org.bumble.core.thread;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.bumble.base.BumbleNameBuilder;
import org.bumble.config.ConfigCenterConst;
import org.bumble.config.Configurator;
import org.bumble.config.ConfiguratorFactory;
import org.bumble.core.BumbleIdentifier;

public class ThreadExecutorGenerator {
	
	private static ThreadExecutorGenerator instance = new ThreadExecutorGenerator();
	
	public ThreadExecutorGenerator() {
		String projName = BumbleNameBuilder.getInstance().getName();
		
		String[] keyChain = null;
		String[] overrideKeyChain = null;
		
		if (BumbleIdentifier.getInstance().isClient()) {
			// Client
			keyChain = new String[] {
				ConfigCenterConst.BumbleClient.NS,
				ConfigCenterConst.BumbleClient.THREAD_POOL_SIZE};
			
			overrideKeyChain = new String[] {
				ConfigCenterConst.BumbleClient.NS,
				ConfigCenterConst.BumbleClient.THREAD_POOL_SIZE,
				projName};
		} else {
			// Manager
			keyChain = new String[] {
				ConfigCenterConst.BumbleManagerServer.NS,
				ConfigCenterConst.BumbleManagerServer.THREAD_POOL_SIZE};
			
			overrideKeyChain = new String[] {
				ConfigCenterConst.BumbleManagerServer.NS,
				ConfigCenterConst.BumbleManagerServer.THREAD_POOL_SIZE,
				projName};
		}
		
		Configurator configurator = ConfiguratorFactory.getConfigurator();
		String threadPoolSizeStr = configurator.getConfig(overrideKeyChain);
		
		if (threadPoolSizeStr == null) {
			threadPoolSizeStr = configurator.getConfig(keyChain);
		}
		
		int threadPoolSize = Integer.valueOf(threadPoolSizeStr);
		threadPool = Executors.newFixedThreadPool(threadPoolSize);
	}
	
	public static ThreadExecutorGenerator getInstance() {
		return instance;
	}
	
	private ExecutorService threadPool = null;
	
	public ExecutorService getExecutor() {
		return threadPool;
	}
}
