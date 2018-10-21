package org.bumble.base.config;

/**
 * Local Configuration Constants
 * @author shenxiangyu
 *
 */
public interface LocalConfigConst {
	
	// Bumble
	public interface Basic {
		public static final String NS = "bumble.basic";
		public static final String NAME = NS + ".name";
		public static final String UNIQ_NAME = NS + ".uniqName";
		public static final String VERSION = NS + ".version";
	}
	
	// Bumble Manager Server
	public interface BumbleManagerServer {
		public static final String NS = "bumble.manager.server";
		public static final String PORT = NS + ".port";
	}
	
	public interface BumbleClient {
		public static final String NS = "bumble.client";
	}
	
	// Configuration Center
	public interface ConfigCenter {
		public static final String NS = "bumble.config";
		public static final String ADDRESS = NS + ".addr";
		public static final String TIMEOUT = NS + ".timeout";
	}
	
	// Log
	public interface Log {
		public static final String NS = "bumble.log";
		public static final String PATH_KEY_LINUX = NS + ".linuxPath";
		public static final String PATH_KEY_WIN = NS + ".winPath";
		public static final String FOLDER_NAME = NS + ".folderName";
		public static final String PROJ_NAME = NS + ".projName";
	}
	
	// reflect
	public interface Reflect {
		public static final String NS = "bumble.reflect";
		public static final String PATH_KEY_LINUX = NS + ".linuxPath";
		public static final String PATH_KEY_WIN = NS + ".winPath";
		public static final String FOLDER_NAME = NS + ".folderName";
		public static final String USE_MULTIPLE_TMP_FILE = NS + ".useMultipleTmpFile";
		public static final String DEL_TMP_FILE = NS + ".delTmpFile";
		public static final String MAPPERS = NS + ".mappers";
	}
}
