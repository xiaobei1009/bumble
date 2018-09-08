package org.bumble.store;

public interface IStoreService {
	public void restart();
	public void set(String key, String value);
	public String get(String key);
}
