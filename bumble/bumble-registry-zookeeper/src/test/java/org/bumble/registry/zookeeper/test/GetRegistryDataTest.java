package org.bumble.registry.zookeeper.test;

import org.bumble.registry.Registry;
import org.bumble.registry.RegistryFactory;
import org.bumble.registry.data.RegistryData;

public class GetRegistryDataTest {
	public static void main( String[] args )
    {
        System.out.println( "GetRegistryDataTest!" );
        Registry registry = RegistryFactory.getRegistry();
        
        RegistryData rd = registry.getData();
        
        System.out.println(rd.toJsonString());
    }
}
