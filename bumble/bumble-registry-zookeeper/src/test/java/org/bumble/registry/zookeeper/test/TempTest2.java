package org.bumble.registry.zookeeper.test;

import org.bumble.registry.Registry;
import org.bumble.registry.RegistryFactory;
import org.bumble.registry.data.RegistryData;

public class TempTest2 {
	
	public static void main( String[] args )
    {
        System.out.println( "Temp Test2!" );
        Registry registry = RegistryFactory.getRegistry();
        
        registry.register("m1", "1.2.3.4:1234");
        registry.register("m2", "1.2.3.4:5678");
        
        registry.bindClientToManager("s1", "1.1.1", "m1");
        registry.bindClientToManager("s2", "2.2.2", "m2");
        
        RegistryData rd = registry.getData();
        
        System.out.println(rd.toJsonString());
    }
}
