package org.bumble.config.zookeeper.test;

import java.util.Map;

import org.bumble.base.BasicConst;
import org.bumble.config.ConfigCenterConst;
import org.bumble.config.Configurator;
import org.bumble.config.ConfiguratorFactory;

public class SetConfigAndGetAllTest {
	
	public static void main( String[] args )
    {
        System.out.println( "SetConfigAndGetAllTest!" );
        
        Configurator configurator = ConfiguratorFactory.getConfigurator();
        
        configurator.clear();
        
        // Redis
        configurator.setConfig(new String[] {
    		ConfigCenterConst.Redis.NS, 
    		ConfigCenterConst.Redis.NODES
    	}, "10.1.44.150:26379,10.1.44.150:26479,10.1.44.150:26579");
        
        configurator.setConfig(new String[] {
    		ConfigCenterConst.Redis.NS, 
    		ConfigCenterConst.Redis.MASTER
    	}, "mymaster");
        
        // Registry
        configurator.setConfig(new String[] {
    		ConfigCenterConst.Registry.NS,
    		ConfigCenterConst.Registry.ADDRESS
    	}, "10.1.44.150:2181");
        
        configurator.setConfig(new String[] {
			ConfigCenterConst.Registry.NS,
    		ConfigCenterConst.Registry.TIMEOUT
    	}, "100000");
        
        // Transaction Manager
        configurator.setConfig(new String[] {
    		ConfigCenterConst.BumbleManagerServer.NS,
    		ConfigCenterConst.BumbleManagerServer.HEART_BEAT_TIMEOUT,
    	}, "120");
        
        configurator.setConfig(new String[] {
    		ConfigCenterConst.BumbleManagerServer.NS,
    		ConfigCenterConst.BumbleManagerServer.HEALTH_CHECK_DELAY,
    	}, "20");
        
        configurator.setConfig(new String[] {
    		ConfigCenterConst.BumbleManagerServer.NS,
    		ConfigCenterConst.BumbleManagerServer.THREAD_POOL_SIZE,
    	}, "100");
        
        configurator.setConfig(new String[] {
        	ConfigCenterConst.BumbleManagerServer.NS,
        	ConfigCenterConst.BumbleManagerServer.TRAN_END_CHECK_TIMEOUT
        }, "10000");
        
        // Client
        configurator.setConfig(new String[] {
    		ConfigCenterConst.BumbleClient.NS,
    		ConfigCenterConst.BumbleClient.HEART_BEAT_TIMEOUT,
    	}, "25");
        
        configurator.setConfig(new String[] {
    		ConfigCenterConst.BumbleClient.NS,
    		ConfigCenterConst.BumbleClient.HEALTH_CHECK_DELAY,
    	}, "20");
        
        configurator.setConfig(new String[] {
    		ConfigCenterConst.BumbleClient.NS,
    		ConfigCenterConst.BumbleClient.THREAD_CONDITION_AWAIT_TIMEOUT,
    	}, "12000");
        
        configurator.setConfig(new String[] {
    		ConfigCenterConst.BumbleClient.NS,
    		ConfigCenterConst.BumbleClient.THREAD_POOL_SIZE,
    	}, "100");
        
        configurator.setConfig(new String[] {
    		ConfigCenterConst.BumbleClient.NS,
    		ConfigCenterConst.BumbleClient.ENABLE_BUMBLE,
    		"C10.18.5.110-demo1",
    	}, BasicConst.YES);
        
        configurator.setConfig(new String[] {
    		ConfigCenterConst.BumbleClient.NS,
    		ConfigCenterConst.BumbleClient.ENABLE_BUMBLE,
    		"C10.18.5.110-demo2",
    	}, BasicConst.YES);
        
        // Core
        configurator.setConfig(new String[] {
    		ConfigCenterConst.BumbleCore.NS,
    		ConfigCenterConst.BumbleCore.HEART_BEAT_INTERVAL
    	}, "5000");
        
        configurator.setConfig(new String[] {
    		ConfigCenterConst.BumbleCore.NS,
    		ConfigCenterConst.BumbleCore.SOCKET_CONNECT_RETRY_INTERVAL
    	}, "5000");
        
        Map<String, String> confMap = configurator.getNamespacedConfig(new String[] {
    		ConfigCenterConst.Registry.NS
    	}, null);
        System.out.println(confMap);
        
        String allConfig = configurator.getAllConfig();
        
        System.out.println(allConfig);
        
    }
}
