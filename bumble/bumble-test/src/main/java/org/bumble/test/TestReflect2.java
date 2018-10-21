package org.bumble.test;

import java.lang.reflect.Method;

import org.bumble.base.test.BumbleTest;
import org.bumble.test.model.Cat;

public class TestReflect2 extends BumbleTest {

	@Override
	public void template(String[] args) throws Exception {
		
	}

	@SuppressWarnings("rawtypes")
	public static void main(String[] args) throws Exception {
		Method[] methods = Cat.class.getMethods();
		for (int i = 0; i < methods.length; i++) {
			Method method = methods[i];
			logger.info("Method Name: " + method.getName());
			
			Class[] paramTypes = method.getParameterTypes();
			for (int j = 0; j < paramTypes.length; j++) {
				Class clazz = paramTypes[j];
				logger.info(clazz.getName());
			}
		}
	}
}
