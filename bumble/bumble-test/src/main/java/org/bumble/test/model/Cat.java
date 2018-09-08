package org.bumble.test.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Cat implements IAnimal, Cloneable {
	
	private String name = null;
	private TestEnum testEnum = TestEnum.FIRST;
	
	public void say(String something) {
		Logger logger = LoggerFactory.getLogger(getClass());
		logger.info("Cat say hi");
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public Object clone() {
		Object obj = null;
		try {
			obj = super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return obj;
	}

	public TestEnum getTestEnum() {
		return testEnum;
	}

	public void setTestEnum(TestEnum testEnum) {
		this.testEnum = testEnum;
	}
	
}
