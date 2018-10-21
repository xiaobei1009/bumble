package com.bumble.test.util;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bumble.test.model.DeepCopyInner;
import org.bumble.test.model.DeepCopyOutter;

public class DeepCopyUtil {
	@SuppressWarnings("unchecked")
	public static <T> T deepCopy(T srcObj) {
		Class<? extends Object> srcClass = (Class<? extends Object>) srcObj.getClass();
		
		T dstObj = null;
		
		try {
			dstObj = (T) srcClass.newInstance();
		} catch (InstantiationException e1) {
			e1.printStackTrace();
		} catch (IllegalAccessException e1) {
			e1.printStackTrace();
		}
		
		Field[] fields = srcClass.getDeclaredFields();
		for (int i = 0; i < fields.length; i++) {
			Field field = fields[i];
			field.setAccessible(true);
			Object srcValue = null;
			try {
				srcValue = field.get(srcObj);
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
			Object dstValue = null;
			if (srcValue != null) {
				if (srcValue instanceof String) {
					dstValue = new String((String)srcValue);
				} else if (srcValue instanceof Cloneable) {
					try {
						dstValue = srcValue.getClass().getMethod("clone").invoke(srcValue);
					} catch (IllegalAccessException e) {
						e.printStackTrace();
					} catch (IllegalArgumentException e) {
						e.printStackTrace();
					} catch (InvocationTargetException e) {
						e.printStackTrace();
					} catch (NoSuchMethodException e) {
						e.printStackTrace();
					} catch (SecurityException e) {
						e.printStackTrace();
					}
				} else {
					dstValue = deepCopy(srcValue);
				}
				try {
					field.set(dstObj, dstValue);
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}
			}
		}
		
		return dstObj;
	}
	
	public static void main(String[] args) {
		DeepCopyInner srcinner = new DeepCopyInner();
		srcinner.setHello("hh1");
		
		Map<String, String> map = new HashMap<String, String>();
		map.put("a", "1");
		srcinner.setHelloMap(map);
		
		List<String> list = new ArrayList<String>();
		list.add("aa");
		srcinner.setHelloList(list);
		
		DeepCopyOutter srcoutter = new DeepCopyOutter();
		srcoutter.setInner(srcinner);
		
		System.out.println("Copy form src to dst");
		DeepCopyOutter dstoutter = deepCopy(srcoutter);
		
		System.out.println("Change dst hello from hh1->hh2");
		dstoutter.getInner().setHello("hh2");
		
		dstoutter.getInner().getHelloMap().put("b", "2");
		System.out.println("Change dst helloMap put {b=2}");
		
		dstoutter.getInner().getHelloList().add("bb");
		System.out.println("Change dst helloList add bb");
		
		System.out.println("src hello: " + srcoutter.getInner().getHello());
		System.out.println("dst hello: " + dstoutter.getInner().getHello());
		
		System.out.println("src helloMap: " + srcoutter.getInner().getHelloMap().toString());
		System.out.println("dst helloMap: " + dstoutter.getInner().getHelloMap().toString());
		
		System.out.println("src helloList: " + srcoutter.getInner().getHelloList().toString());
		System.out.println("dst helloList: " + dstoutter.getInner().getHelloList().toString());
	}
}
