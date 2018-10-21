package org.bumble.test.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Tiger implements IAnimal {

	public void say(String something) {
		Logger logger = LoggerFactory.getLogger(getClass());
		logger.info("Tiger say hi");
	}
	
}
