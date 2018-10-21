package org.bumble.test;

import org.bumble.base.test.BumbleTest;
import org.bumble.test.model.Cat;

public class CopyTest extends BumbleTest {

	@Override
	public void template(String[] args) throws Exception {
		
	}

	public static void main(String[] args) {
		System.err.println("xxx");
		Cat cat = new Cat();
		cat.setName("xxx");
		Cat cat2 = (Cat) cat.clone();
		cat2.setName("yyy");
		
		logger.info(cat.getName() + cat2.getName());
		
	}
}
