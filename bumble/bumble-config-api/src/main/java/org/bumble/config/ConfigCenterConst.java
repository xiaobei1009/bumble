package org.bumble.config;

public interface ConfigCenterConst {
	public static final String ROOT = "root";
	
	public interface Registry {
		public static final String NS = "registry";
		
		/**
		 * Registry address
		 * <p>
		 * e.g. Ip1:Port1,Ip2:Port2
		 */
		public static final String ADDRESS = "address";
		
		/**
		 * Registry connection timeout (unit: millisecond)
		 * <p>
		 * e.g. 5000
		 */
		public static final String TIMEOUT = "timeout";
		
		/**
		 * Increasing Integer when this is changed then leader will reload the registry
		 */
		public static final String RELOAD_FLAG = "0";
	}
	
	public interface BumbleManagerServer {
		public static final String NS = "manager-socket";
		
		/**
		 * Heart beat timeout (unit: second)
		 * <p>
		 * e.g. 25
		 */
		public static final String HEART_BEAT_TIMEOUT = "heart-beat-timeout";
		
		/**
		 * Health check delay (unit: second)
		 * <p>
		 * e.g. 20
		 */
		public static final String HEALTH_CHECK_DELAY = "health-check-delay";
		
		/**
		 * Thread pool size
		 * <p>
		 * This will take effect from start up, and no action will take when <br>
		 * the configure changed until re-start.<br>
		 * e.g. 100<br>
		 * It has sub configuration grouped by project name which can override the base line<br>
		 * e.g. thread-pool-size/{project-name}=200
		 */
		public static final String THREAD_POOL_SIZE = "thread-pool-size";
		
		/**
		 * Transaction end check timeout
		 * <p>
		 * At the end of the transaction on manager side triggered by the transaction starter,<br>
		 * the manager will check if all the participants are ready, and a timer with a timeout<br>
		 * (timeout figure is set by this configure).<br>
		 * The check will be triggered when each participant state action arrives.<br>
		 * Unit: millisecond
		 */
		public static final String TRAN_END_CHECK_TIMEOUT = "tran-end-check-timeout";
	}
	
	public interface BumbleCore {
		public static final String NS = "core";
		
		/**
		 * Health beat interval (unit: millisecond)
		 * <p>
		 * e.g. 5000
		 */
		public static final String HEART_BEAT_INTERVAL = "heart-beat-interval";
		
		/**
		 * Socket connect retry interval
		 * e.g. 5000
		 */
		public static final String SOCKET_CONNECT_RETRY_INTERVAL = "socket-connect-retry-interval";
	}
	
	public interface BumbleClient {
		public static final String NS = "clients";
		
		/**
		 * Heart beat timeout (unit: second)
		 * <p>
		 * e.g. 25
		 */
		public static final String HEART_BEAT_TIMEOUT = "heart-beat-timeout";
		
		/**
		 * Health check delay (unit: second)
		 * <p>
		 * e.g. 20
		 */
		public static final String HEALTH_CHECK_DELAY = "health-check-delay";
		
		/**
		 * Enable bumble flag
		 * <p>
		 * e.g. enable-bumble/{ProjName}=y/n
		 */
		public static final String ENABLE_BUMBLE = "enbale-bumble";
		
		/**
		 * When BumbleCondition start await, there will be a timer scheduled to throw
		 * error when it times out. And this is the timeout limit configuration.
		 */
		public static final String THREAD_CONDITION_AWAIT_TIMEOUT = "thread-condition-await-timeout";
		
		/**
		 * Thread pool size
		 * <p>
		 * This will take effect from start up, and no action will take when <br>
		 * the configure changed until re-start.<br>
		 * e.g. 100<br>
		 * It has sub configuration grouped by project name which can override the base line<br>
		 * e.g. thread-pool-size/{project-name}=200
		 */
		public static final String THREAD_POOL_SIZE = "thread-pool-size";
	}
	
	public interface Redis {
		public static final String NS = "redis";
		
		/**
		 * Redis nodes for sentinel mode
		 */
		public static final String NODES = "nodes";
		
		/**
		 * Redis master for sentinel mode
		 */
		public static final String MASTER = "master";
	}
}
