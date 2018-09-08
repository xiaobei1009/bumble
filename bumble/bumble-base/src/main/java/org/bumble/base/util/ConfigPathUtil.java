package org.bumble.base.util;

import java.io.File;

public class ConfigPathUtil {
	
	private static final String CONFIG_FILE_PATH = "conf";
	
	public static String getConfigPath() {
		
		String absPath = new File("").getAbsolutePath();
		String parentPath = absPath.substring(0, absPath.lastIndexOf(File.separator)); 
		String filePath = String.join(File.separator, parentPath, CONFIG_FILE_PATH);
		
		return filePath;
	}
}
