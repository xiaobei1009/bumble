package org.bumble.config.zookeeper.test;

import java.util.Map;

import org.bumble.config.Configurator;
import org.bumble.config.ConfiguratorFactory;

public class TestNsConfig {
	public static void main( String[] args )
    {
        System.out.println( "Test Ns Config!" );
        
        Configurator configurator = ConfiguratorFactory.getConfigurator();

        configurator.setConfig(new String[] {"manager-socket", "netty-idle-timeout"}, "15");
        configurator.setConfig(new String[] {"manager-socket", "heart-beat-timeout"}, "25");
        
        Map<String, String> map = configurator.getNamespacedConfig(new String[] {}, null);
        
        System.out.println(map);
        
        Map<String, String> map2 = configurator.getNamespacedConfig(new String[] {"clients"}, null);
        
        System.out.println(map2);
    }
}
