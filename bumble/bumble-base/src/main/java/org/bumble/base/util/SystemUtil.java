package org.bumble.base.util;

public class SystemUtil {
	
	public static Boolean isWindowsOs() {
		String osName = System.getProperty("os.name");
		if (osName == null) {
			return false;
		}
		
		return osName.startsWith("Windows");
	}
}
