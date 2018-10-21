package org.bumble.test;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import org.bumble.base.test.BumbleTest;
import org.bumble.test.model.Cat;
import org.bumble.test.model.IAnimal;

public class ProxyTest extends BumbleTest {

	@Override
	public void template(String[] args) throws Exception {
	
		
	}
	
	public static void main(String[] args) throws Exception {
		final Cat cat = new Cat();
		
		IAnimal x = (IAnimal) Proxy.newProxyInstance(IAnimal.class.getClassLoader(), new Class[] {IAnimal.class}, new InvocationHandler() {

			public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
				String methodName = method.getName();
				System.out.println(methodName);
				for (Object arg : args) {
					System.out.println(arg);
				}
				
				method.invoke(cat, args);
				
				return null;
			}
			
		});
		
		x.say("hello");
	}
}
