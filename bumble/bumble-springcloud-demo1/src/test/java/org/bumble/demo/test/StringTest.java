package org.bumble.demo.test;

import org.bumble.base.test.BumbleTest;

public class StringTest extends BumbleTest {
	public static void main(String[] args) {
		String name = "abcdef-12345";
		name = name.substring(0, name.lastIndexOf("-"));
		logger.info(name);
		
		name = "abcdef12345";
		if (name.lastIndexOf("-") > 0) {
			name = name.substring(0, name.lastIndexOf("-"));
			logger.info(name);
		}
	}

	@Override
	public void template(String[] args) throws Exception {
		
	}
}
