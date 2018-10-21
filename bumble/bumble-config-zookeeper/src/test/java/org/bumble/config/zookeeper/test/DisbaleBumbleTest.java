package org.bumble.config.zookeeper.test;

import java.util.Map;

import org.bumble.base.BasicConst;
import org.bumble.config.ConfigCenterConst;
import org.bumble.config.Configurator;
import org.bumble.config.ConfiguratorFactory;

public class DisbaleBumbleTest {
	
	public static void main( String[] args )
    {
        System.out.println( "SetConfigAndGetAllTest!" );
        
        Configurator configurator = ConfiguratorFactory.getConfigurator();
        
        configurator.setConfig(new String[] {
    		ConfigCenterConst.BumbleClient.NS,
    		ConfigCenterConst.BumbleClient.ENABLE_BUMBLE,
    		"C10.18.5.110-demo1",
    	}, BasicConst.NO);
        
        configurator.setConfig(new String[] {
    		ConfigCenterConst.BumbleClient.NS,
    		ConfigCenterConst.BumbleClient.ENABLE_BUMBLE,
    		"C10.18.5.110-demo2",
    	}, BasicConst.NO);
        
        Map<String, String> confMap = configurator.getNamespacedConfig(new String[] {
    		ConfigCenterConst.Registry.NS
    	}, null);
        System.out.println(confMap);
        
        String allConfig = configurator.getAllConfig();
        
        System.out.println(allConfig);
        
    }
}
