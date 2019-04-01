package org.bumble.store;

public interface IStoreService {
	void restart();
	void set(String key, String value);
	String get(String key);
}
