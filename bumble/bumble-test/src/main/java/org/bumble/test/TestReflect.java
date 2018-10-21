package org.bumble.test;

import java.util.List;

import org.bumble.base.BasicConst;
import org.bumble.base.config.LocalConfigConst;
import org.bumble.base.config.LocalConfigHolder;
import org.bumble.base.test.BumbleTest;
import org.bumble.base.util.ReflectUtil;
import org.bumble.test.model.IAnimal;

public class TestReflect extends BumbleTest {

	@Override
	public void template(String[] args) throws Exception {
		
	}

	public static void main(String[] args) throws Exception {
		List<Class> classes = ReflectUtil.getImplementClasses4Interface(IAnimal.class);
		logger.info(classes.toString());
		
		IAnimal animal = ReflectUtil.getInstance(IAnimal.class);
		
		String interfaceMapperKey = String.join(BasicConst.POINT, LocalConfigConst.Reflect.MAPPERS, IAnimal.class.getName());
		logger.info(interfaceMapperKey);
		
		String implementClassName = LocalConfigHolder.getInstance().getConfig(interfaceMapperKey);
		logger.info(implementClassName);
		
		animal.say("nothing else");
	}
}
