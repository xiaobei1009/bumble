package org.bumble.core.thread;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.bumble.base.BumbleNameBuilder;
import org.bumble.config.ConfigCenterConst;
import org.bumble.config.Configurator;
import org.bumble.config.ConfiguratorFactory;
import org.bumble.core.BumbleIdentifier;

/**
 * ThreadExecutorGenerator
 * 
 * @author shenxiangyu
 *
 */
public class ThreadExecutorGenerator {
	
	private volatile static ThreadExecutorGenerator instance = new ThreadExecutorGenerator();
	
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
		
		ThreadFactory namedThreadFactory = new ThreadFactory() {
			
			SecurityManager s = System.getSecurityManager();

	        private final ThreadGroup group =
	        	(s != null) ?
	        	s.getThreadGroup() :
                Thread.currentThread().getThreadGroup();
            
	        private final AtomicInteger threadNumber = new AtomicInteger(1);
	        private final String namePrefix = "bumble-thread-pool-thread-";
	        
	        public Thread newThread(Runnable r) {
				Thread t = new Thread(group, r, namePrefix + threadNumber.getAndIncrement(), 0);
				if (t.isDaemon()) {
					t.setDaemon(false);
				}
				if (t.getPriority() != Thread.NORM_PRIORITY) {
					t.setPriority(Thread.NORM_PRIORITY);
				}
				return t;
			}
			
		};
		
		threadPool = new ThreadPoolExecutor(threadPoolSize, threadPoolSize,
	        0L, TimeUnit.MILLISECONDS,
	        new LinkedBlockingQueue<Runnable>(1024), namedThreadFactory, new ThreadPoolExecutor.AbortPolicy());
		    
	}
	
	public static ThreadExecutorGenerator getInstance() {
		return instance;
	}
	
	private ExecutorService threadPool = null;
	
	public ExecutorService getExecutor() {
		return threadPool;
	}
}
