package org.bumble.base.log;

import java.io.File;
import java.net.URL;
import java.util.Map;

import org.bumble.base.config.LocalConfigConst;
import org.bumble.base.config.LocalConfigHolder;
import org.bumble.base.util.ConfigPathUtil;
import org.bumble.base.util.ResourceUrlUtil;
import org.bumble.base.util.SystemUtil;
import org.slf4j.impl.StaticLoggerBinder;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.util.ContextInitializer;

public class LogInitializer {
	
	private final static String FILE_LOG_PATH = "logback" + File.separator + "logback.xml";
	private final static String CP_LOG_CONTEXT_PATH = "classpath:logback/logback.xml";
	
	private void setLogPath() {
		Map<String, String> props = LocalConfigHolder.getInstance().getConfigForNamespace(LocalConfigConst.Log.NS);
		
		String rootPath = "";
		String folderName = props.get(LocalConfigConst.Log.FOLDER_NAME);
		String projName = props.get(LocalConfigConst.Log.PROJ_NAME);
		
		if (SystemUtil.isWindowsOs()) {
			rootPath = props.get(LocalConfigConst.Log.PATH_KEY_WIN);
		} else {
			rootPath = props.get(LocalConfigConst.Log.PATH_KEY_LINUX);
		}
		String path = rootPath + folderName + File.separator + projName;
		
		System.out.println("[" + projName + "] [logPathRoot] = " + path);
		System.setProperty("log.path.root", path);
	}
	
	private void loadLogContext() {
        URL url = null;
		try {
			String filePath = String.join(File.separator, ConfigPathUtil.getConfigPath(), FILE_LOG_PATH);
			File file = new File(filePath);
			if (file.exists()) {
				System.out.println("Use log config under file");
				url = file.toURI().toURL();
			} else {
				System.out.println("Use log config under resource");
				url = ResourceUrlUtil.getURL(CP_LOG_CONTEXT_PATH);
			}
			System.out.println("LogConfigFile Location: " + url.toString());
			LoggerContext loggerContext = (LoggerContext)StaticLoggerBinder.getSingleton().getLoggerFactory();
			
			loggerContext.reset();
			
	        new ContextInitializer(loggerContext).configureByResource(url);
			
		} catch (Exception e) {
			System.out.println("Log conetext load failed!");
		}
	}
	
	public void init() {
		setLogPath();
		
		loadLogContext();
	}
}
