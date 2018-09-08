package org.bumble.test;

import java.util.Map;

import org.bumble.base.config.LocalConfigHolder;

public class PropertyReadTest {
	
	public static void main(String[] args) throws Exception {
		Map<String, String> map = LocalConfigHolder.getInstance().getAllConfig();
		System.out.println(map.keySet().size());
	}
	
}
