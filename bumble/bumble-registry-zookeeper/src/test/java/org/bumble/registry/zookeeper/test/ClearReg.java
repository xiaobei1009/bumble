package org.bumble.registry.zookeeper.test;

import org.bumble.registry.Registry;
import org.bumble.registry.RegistryFactory;

public class ClearReg {
	public static void main( String[] args )
    {
        System.out.println( "Clear Reg!" );
        Registry registry = RegistryFactory.getRegistry();
        
        registry.clear();
        System.out.println( "Clear Reg end!" );
    }
}
