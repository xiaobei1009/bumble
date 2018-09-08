package org.bumble.test;

import java.lang.reflect.Method;

import org.bumble.base.test.BumbleTest;
import org.bumble.test.model.Cat;
import org.bumble.test.model.TestEnum;

public class EnumTypeTest extends BumbleTest {

	@Override
	public void template(String[] args) throws Exception {
		
	}

	public static void main(String[] args) throws Exception {
		TestEnum tn = TestEnum.valueOf("FIRST");
		switch (tn) {
			case FIRST:
				logger.info("first");
				break;
			case SECOND:
				logger.info("second");
				break;
		}
		
		for (Method mthd : Cat.class.getMethods()) {
			String mthdName = mthd.getName();
			logger.info("MethodName: " + mthdName);
			
			Class[] pts = mthd.getParameterTypes();
			if (pts.length > 0) {
				Class pt = pts[0];
				String ptName = pt.getSimpleName();
				logger.info(ptName);
			}
		}
	}
}
