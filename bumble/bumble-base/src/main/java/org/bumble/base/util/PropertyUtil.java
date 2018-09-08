package org.bumble.base.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

public class PropertyUtil {
	
	@SuppressWarnings("rawtypes")
	public static Map<String, String> getProperties(String fileName) {
		Properties prop = new Properties();
		Map<String, String> map = new HashMap<String, String>();
		try { 
			String filePath = String.join(File.separator, ConfigPathUtil.getConfigPath(), fileName);
			File file = new File(filePath);
			InputStream input = null;
			
			if (file.exists()) {
				System.out.println("Using config under file: " + filePath);
				input = new FileInputStream(file);
			} else {
				System.out.println("Using config under resources: " + PropertyUtil.class.getClassLoader().getResource(fileName).toString());
				input = PropertyUtil.class.getClassLoader().getResourceAsStream(fileName);
			}
			prop.load(new InputStreamReader(input, "UTF-8"));   
			if (prop.size() > 0) {
				Iterator iter = prop.keySet().iterator();
				while (iter.hasNext()) {
					String key = (String) iter.next();
					String value = prop.getProperty(key);
					map.put(key, value);
				}
			}
		} catch (Exception ex) {
			System.out.println("[" + fileName + "] file not exists.");
			ex.printStackTrace();
		}
		
		return map;
	}
	
}
