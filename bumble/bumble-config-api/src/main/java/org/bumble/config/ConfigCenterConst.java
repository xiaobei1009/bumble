package org.bumble.config;

public interface ConfigCenterConst {
	String ROOT = "root";
	
	interface Registry {
		String NS = "registry";
		
		/**
		 * Registry address
		 * <p>
		 * e.g. Ip1:Port1,Ip2:Port2
		 */
		String ADDRESS = "address";
		
		/**
		 * Registry connection timeout (unit: millisecond)
		 * <p>
		 * e.g. 5000
		 */
		String TIMEOUT = "timeout";
		
		/**
		 * Increasing Integer when this is changed then leader will reload the registry
		 */
		String RELOAD_FLAG = "0";
	}
	
	interface BumbleManagerServer {
		String NS = "manager-socket";
		
		/**
		 * Heart beat timeout (unit: second)
		 * <p>
		 * e.g. 25
		 */
		String HEART_BEAT_TIMEOUT = "heart-beat-timeout";
		
		/**
		 * Health check delay (unit: second)
		 * <p>
		 * e.g. 20
		 */
		String HEALTH_CHECK_DELAY = "health-check-delay";
		
		/**
		 * Thread pool size
		 * <p>
		 * This will take effect from start up, and no action will take when <br>
		 * the configure changed until re-start.<br>
		 * e.g. 100<br>
		 * It has sub configuration grouped by project name which can override the base line<br>
		 * e.g. thread-pool-size/{project-name}=200
		 */
		String THREAD_POOL_SIZE = "thread-pool-size";
		
		/**
		 * Transaction end check timeout
		 * <p>
		 * At the end of the transaction on manager side triggered by the transaction starter,<br>
		 * the manager will check if all the participants are ready, and a timer with a timeout<br>
		 * (timeout figure is set by this configure).<br>
		 * The check will be triggered when each participant state action arrives.<br>
		 * Unit: millisecond
		 */
		String TRAN_END_CHECK_TIMEOUT = "tran-end-check-timeout";
	}
	
	interface BumbleCore {
		String NS = "core";
		
		/**
		 * Health beat interval (unit: millisecond)
		 * <p>
		 * e.g. 5000
		 */
		String HEART_BEAT_INTERVAL = "heart-beat-interval";
		
		/**
		 * Socket connect retry interval
		 * e.g. 5000
		 */
		String SOCKET_CONNECT_RETRY_INTERVAL = "socket-connect-retry-interval";
	}
	
	interface BumbleClient {
		String NS = "clients";
		
		/**
		 * Heart beat timeout (unit: second)
		 * <p>
		 * e.g. 25
		 */
		String HEART_BEAT_TIMEOUT = "heart-beat-timeout";
		
		/**
		 * Health check delay (unit: second)
		 * <p>
		 * e.g. 20
		 */
		String HEALTH_CHECK_DELAY = "health-check-delay";
		
		/**
		 * Enable bumble flag
		 * <p>
		 * e.g. enable-bumble/{ProjName}=y/n
		 */
		String ENABLE_BUMBLE = "enbale-bumble";
		
		/**
		 * When BumbleCondition start await, there will be a timer scheduled to throw
		 * error when it times out. And this is the timeout limit configuration.
		 */
		String THREAD_CONDITION_AWAIT_TIMEOUT = "thread-condition-await-timeout";
		
		/**
		 * Thread pool size
		 * <p>
		 * This will take effect from start up, and no action will take when <br>
		 * the configure changed until re-start.<br>
		 * e.g. 100<br>
		 * It has sub configuration grouped by project name which can override the base line<br>
		 * e.g. thread-pool-size/{project-name}=200
		 */
		String THREAD_POOL_SIZE = "thread-pool-size";
	}
	
	interface Redis {
		String NS = "redis";
		
		/**
		 * Redis nodes for sentinel mode
		 */
		String NODES = "nodes";
		
		/**
		 * Redis master for sentinel mode
		 */
		String MASTER = "master";
	}
}
