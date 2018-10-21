package org.bumble.store;

import org.bumble.base.util.ReflectUtil;

public class StoreServiceFactory {
	private volatile static StoreServiceFactory instance = null;
	public StoreServiceFactory getInstance() {
		if (instance == null) {
			instance = new StoreServiceFactory();
		}
		return instance;
	}
	public static IStoreService getStoreService() {
		IStoreService storeService = ReflectUtil.getInstance(IStoreService.class);
		return storeService;
	}
}
