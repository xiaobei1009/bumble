package org.bumble.client.condition;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.bumble.base.util.UuidUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.bumble.config.ConfigCenterConst;
import org.bumble.config.ConfigChangedNotifier;
import org.bumble.config.ConfiguratorFactory;

public class BumbleConditionFactory {
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	private volatile static BumbleConditionFactory instance = null;
	
	private int threadConditionAwaitTimeout = 0;
	
	public BumbleConditionFactory() {
		final BumbleConditionFactory _this = this;
		
		// Get configuration
		String threadConditionAwaitTimeoutStr = ConfiguratorFactory.getConfigurator().getConfig(new String[] {
    		ConfigCenterConst.BumbleClient.NS,
    		ConfigCenterConst.BumbleClient.THREAD_CONDITION_AWAIT_TIMEOUT}, new ConfigChangedNotifier() {
				public void doNotify(String key, String value) {
					_this.setThreadConditionAwaitTimeout(Integer.valueOf(value));
				}
		});
		setThreadConditionAwaitTimeout(Integer.valueOf(threadConditionAwaitTimeoutStr));
	}
	
	public static BumbleConditionFactory getInstance() {
		if (instance == null) {
			instance = new BumbleConditionFactory();
		}
		return instance;
	}
	
	private Map<String, BumbleCondition> conditions = new ConcurrentHashMap<String, BumbleCondition>();
	
	public BumbleCondition newCondition() {
		String id = UuidUtil.uuid();
		logger.debug("Create new condition: " + id);
		
		BumbleCondition condition = new BumbleCondition(id);
		condition.setAwaitTimeout(threadConditionAwaitTimeout);
		
		conditions.put(id, condition);
		return condition;
	}
	
	public void updateConditionAwaitTimeout() {
		for (String id : conditions.keySet()) {
			BumbleCondition condition = conditions.get(id);
			condition.setAwaitTimeout(threadConditionAwaitTimeout);
		}
	}
	
	public BumbleCondition getCondition(String id) {
		return conditions.get(id);
	}
	
	public void remove(BumbleCondition condition) {
		remove(condition.getId());
	}
	
	public void remove(String id) {
		BumbleCondition condition = conditions.get(id);
		if (condition == null)
			return;
		
		if (condition.isAwaiting()) {
			condition.setState(BumbleCondition.State.TERMINATED);
			condition.signal();
		}
		conditions.remove(id);
	}

	public int getThreadConditionAwaitTimeout() {
		return threadConditionAwaitTimeout;
	}

	public void setThreadConditionAwaitTimeout(int threadConditionAwaitTimeout) {
		this.threadConditionAwaitTimeout = threadConditionAwaitTimeout;
	}
}
