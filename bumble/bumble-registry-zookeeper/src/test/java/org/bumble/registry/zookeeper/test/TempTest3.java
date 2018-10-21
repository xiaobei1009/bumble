package org.bumble.registry.zookeeper.test;

import java.util.Map;

import org.bumble.base.test.BumbleTest;

import org.bumble.registry.Registry;
import org.bumble.registry.RegistryFactory;
import org.bumble.registry.data.RegistryData;

public class TempTest3 extends BumbleTest {

	public static void main( String[] args )
    {
		Registry registry = RegistryFactory.getRegistry();
		RegistryData rd = registry.getData();
		Map<String, String> map = rd.getClientMngrMap();
		logger.info(map.toString());
    }

	@Override
	public void template(String[] args) throws Exception {
		
	}
    
}
