package org.bumble.config.zookeeper.test;

import org.bumble.config.Configurator;
import org.bumble.config.ConfiguratorFactory;

public class TestRunConfWatcher {
	public static void main( String[] args ) throws InterruptedException
    {
        System.out.println( "Run config Watcher" );
        
        ConfigChangeWatcher watcher = new ConfigChangeWatcher();
        Thread watcherThread = new Thread(watcher);
        watcherThread.start();
        
        Configurator configurator = ConfiguratorFactory.getConfigurator();

        Thread.sleep(5000);
        
        System.out.println("change");
        configurator.setConfig(new String[] {"kkk"}, "111");
        
        System.out.println("end");
    }
}
