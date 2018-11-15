package org.bumble.store.dummy;

import org.bumble.store.IStoreService;

/**
 * @author : shenxiangyu
 * @date : 2018-11-15
 */
public class DummyStoreService implements IStoreService {
    @Override
    public void restart() {

    }

    @Override
    public void set(String key, String value) {

    }

    @Override
    public String get(String key) {
        return null;
    }
}
