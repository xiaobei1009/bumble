package org.bumble.core;

import java.io.IOException;
import java.io.InputStream;

import org.bumble.base.BumbleNameBuilder;
import org.bumble.base.Callback;
import org.bumble.base.config.LocalConfigConst;
import org.bumble.base.config.LocalConfigHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BumbleLogoPrinter {
	
	private static Logger logger = LoggerFactory.getLogger(BumbleLogoPrinter.class);
	private static final String BUMBLE_LOGO_FILE = "bumble-logo/bumble-logo";
	
	public static void print() {
		print(null);
	}
	
	public static void print(Callback replacementCallback) {
		String logoFileLocation = BumbleLogoPrinter.class.getClassLoader().getResource(BUMBLE_LOGO_FILE).toString();
		logger.debug("LogoFileLocation: {}", logoFileLocation);
		InputStream is = BumbleLogoPrinter.class.getClassLoader().getResourceAsStream(BUMBLE_LOGO_FILE);
		String logoStr = null;
		byte[] bytes = new byte[0];
		try {
			bytes = new byte[is.available()];
			is.read(bytes);
			logoStr = new String(bytes);
			
			String uniqName = BumbleNameBuilder.getInstance().getUniqName();
			logoStr = logoStr.replace("{{name}}", uniqName);
			
			String version = LocalConfigHolder.getInstance().getConfig(LocalConfigConst.Basic.VERSION);
			logoStr = logoStr.replace("{{version}}", version);
			
			if (replacementCallback != null) {
				logoStr = (String) replacementCallback.doCallback(logoStr);
			}
			
			logger.info(logoStr);
		} catch (IOException e) {
			logger.error(e.getMessage());
			logger.trace(e.getMessage(), e);
		}
	}
}
