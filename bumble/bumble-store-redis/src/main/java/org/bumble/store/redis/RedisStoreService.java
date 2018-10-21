package org.bumble.store.redis;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.bumble.config.ConfigCenterConst;
import org.bumble.config.ConfigChangedNotifier;
import org.bumble.config.Configurator;
import org.bumble.config.ConfiguratorFactory;
import org.bumble.store.IStoreService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisSentinelPool;

public class RedisStoreService implements IStoreService {
	
	Logger logger = LoggerFactory.getLogger(this.getClass());
	
	private JedisSentinelPool jedisPool = null;
	private Map<String, String> configMap = null;
	
	public RedisStoreService() {
		Configurator configurator = ConfiguratorFactory.getConfigurator();
		configMap = configurator.getNamespacedConfig(new String[] {ConfigCenterConst.Redis.NS}, new ConfigChangedNotifier() {
			public void doNotify(String key, String value) {
				logger.info("redis configuration changed.");
				configMap.put(key, value);
			}
		});
		start();
	}
	
	private void start() {
		String redisMaster = configMap.get(String.join(".", 
				ConfigCenterConst.ROOT, 
				ConfigCenterConst.Redis.NS, 
				ConfigCenterConst.Redis.MASTER));
		
		String redisNodes = configMap.get(String.join(".", 
				ConfigCenterConst.ROOT, 
				ConfigCenterConst.Redis.NS, 
				ConfigCenterConst.Redis.NODES));
		
		String[] hostArr = redisNodes.split(",");
		Set<String> hostSet = new HashSet<String>();
		for (String host : hostArr) {
			hostSet.add(host);
		}
		
		// Initialize REDIS configuration
		JedisPoolConfig poolConfig = new JedisPoolConfig();
		
		if (jedisPool != null) {
			jedisPool.close();
		}
		try {
			jedisPool = new JedisSentinelPool(redisMaster, hostSet, poolConfig, 2000, null, 1, null);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}
	
	public void set(String key, String value) {
		if (jedisPool == null) {
			return;
		}
		jedisPool.getResource().set(key, value);
	}

	public String get(String key) {
		if (jedisPool == null) {
			return null;
		}
		return jedisPool.getResource().get(key);
	}

	public void restart() {
		start();
	}

}
