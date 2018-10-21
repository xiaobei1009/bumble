package org.bumble.test;

import java.util.HashMap;
import java.util.Map;

import org.bumble.base.test.BumbleTest;
import org.bumble.test.model.ObjectHashKey;

public class ObjectHashKeyTest extends BumbleTest {

	@Override
	public void template(String[] args) throws Exception {
		
	}
	
	public static void main(String[] args) throws Exception {
		Map<ObjectHashKey, String> ohkMap = new HashMap<ObjectHashKey, String>();
		
		ObjectHashKey ohk = new ObjectHashKey();
		ohk.setAttr1("111");
		
		ObjectHashKey ohk2 = new ObjectHashKey();
		ohk2.setAttr1("222");
		
		ohkMap.put(ohk, "111");
		logger.info("{}", ohkMap.values().size());
		
		ohkMap.put(ohk2, "222");
		logger.info("{}", ohkMap.values().size());
	}
}
