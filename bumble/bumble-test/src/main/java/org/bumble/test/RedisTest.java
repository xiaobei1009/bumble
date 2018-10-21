package org.bumble.test;

import org.bumble.base.test.BumbleTest;
import org.bumble.store.IStoreService;
import org.bumble.store.StoreServiceFactory;

public class RedisTest extends BumbleTest {

	@Override
	public void template(String[] args) throws Exception {
		
	}
	
	public static void main(String[] args) throws Exception {
		IStoreService storeService = StoreServiceFactory.getStoreService();
		storeService.set("kk", "11");
		
		String ret = storeService.get("kk");
		logger.info(ret);
	}
}
