package org.bumble.base.config;

/**
 * Local Configuration Constants
 * @author shenxiangyu
 *
 */
public interface LocalConfigConst {
	
	// Bumble
	interface Basic {
		String NS = "bumble.basic";
		String NAME = NS + ".name";
		String UNIQ_NAME = NS + ".uniqName";
		String VERSION = NS + ".version";
	}
	
	// Bumble Manager Server
	interface BumbleManagerServer {
		String NS = "bumble.manager.server";
		String PORT = NS + ".port";
	}
	
	interface BumbleClient {
		String NS = "bumble.client";
	}
	
	// Configuration Center
	interface ConfigCenter {
		String NS = "bumble.config";
		String ADDRESS = NS + ".addr";
		String TIMEOUT = NS + ".timeout";
	}
	
	// Log
	interface Log {
		String NS = "bumble.log";
		String PATH_KEY_LINUX = NS + ".linuxPath";
		String PATH_KEY_WIN = NS + ".winPath";
		String FOLDER_NAME = NS + ".folderName";
		String PROJ_NAME = NS + ".projName";
	}
	
	// reflect
	interface Reflect {
		String NS = "bumble.reflect";
		String PATH_KEY_LINUX = NS + ".linuxPath";
		String PATH_KEY_WIN = NS + ".winPath";
		String FOLDER_NAME = NS + ".folderName";
		String USE_MULTIPLE_TMP_FILE = NS + ".useMultipleTmpFile";
		String DEL_TMP_FILE = NS + ".delTmpFile";
		String MAPPERS = NS + ".mappers";
	}
}
