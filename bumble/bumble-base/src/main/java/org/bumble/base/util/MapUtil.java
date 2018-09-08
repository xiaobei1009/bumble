package org.bumble.base.util;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.sql.Clob;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class MapUtil {

	private static final Logger logger = LoggerFactory.getLogger(MapUtil.class);
	
	/**
	 * HashMap to Bean
	 * @param map
	 * @param beanClass
	 * @param includeFields
	 * @return
	 * @throws Exception
	 */
	public static Object mapToObject(Map<String, Object> map, Class<?> beanClass) throws Exception {
		return MapUtil.mapToObject(map, beanClass, false);
	}
	
	/**
	 * HashMap to Bean
	 * @param map
	 * @param beanClass
	 * @param includeFields
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static Object mapToObject(Map<String, Object> map, Class<?> beanClass, Boolean printLog) throws Exception {
		if (map == null)
			return null;
		
		if (printLog)
			logger.debug("------mapToObject start------" + beanClass.getName());
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Map<String, Object> lowCaseKeyMap = MapUtil.toLowerCaseKeyMap(map);
		
		Object obj = beanClass.newInstance();
		Field[] fields = beanClass.getDeclaredFields();
		
		for (Field field : fields) {
			String fieldName = field.getName();
			if (printLog)
				logger.debug("field Name : " + fieldName);
			
			String modifier = Modifier.toString(field.getModifiers());
			if (modifier.equals("private static final"))
				continue;
			
			Object mapItem = lowCaseKeyMap.get(fieldName.toLowerCase());
			if (mapItem == null || mapItem.toString().equals("null"))
				continue;
			
			field.setAccessible(true);
			String mapItemStr = mapItem.toString();
			
			Class fieldClass = field.getType();
			
			if (fieldClass.equals(Long.class) || fieldClass.equals(long.class)) {
				mapItemStr = mapItemStr.isEmpty() ? "0" : mapItemStr;
				field.set(obj, Long.valueOf(mapItemStr));
			} else if (fieldClass.equals(Integer.class) || fieldClass.equals(int.class)) {
				mapItemStr = mapItemStr.isEmpty() ? "0" : mapItemStr;
				field.set(obj, Integer.valueOf(mapItemStr));
			} else if (fieldClass.equals(Boolean.class) || fieldClass.equals(boolean.class)) {
				field.set(obj, Boolean.valueOf(mapItemStr));
			} else if (fieldClass.equals(Date.class)) {
				Date date = null;
				try {
					date = sdf.parse(mapItemStr);
					field.set(obj, date);
				} catch(Exception e) {}
			} else if (fieldClass.equals(Double.class) || fieldClass.equals(double.class)) {
				mapItemStr = mapItemStr.isEmpty() ? "0" : mapItemStr;
				field.set(obj, Double.valueOf(mapItemStr));
			} else if (fieldClass.equals(Float.class) || fieldClass.equals(float.class)) {
				mapItemStr = mapItemStr.isEmpty() ? "0" : mapItemStr;
				field.set(obj, Float.valueOf(mapItemStr));
			} else if (fieldClass.equals(Clob.class)) {
				field.set(obj, mapItem);
			} else if (fieldClass.equals(String.class)) {
				field.set(obj, mapItemStr);
			} else if (fieldClass.equals(List.class)) {
				Type fieldGenType = field.getGenericType();
				if (fieldGenType instanceof ParameterizedType) {
					ParameterizedType pType = (ParameterizedType) fieldGenType;
					Type[] actualTypes = pType.getActualTypeArguments();
					for (Type actualType : actualTypes) {
						if (actualType instanceof Class) {
							Class listGenTypClz = (Class) actualType;
							if (printLog)
								logger.debug("field list generic type Name : " + listGenTypClz.getName());
							
							List<Object> listObj = new ArrayList<Object>();
							
							// Loop the specific list in the map to handle each item in the list
							for (Object listItm : (List)mapItem) {
								Object listItmObj = null;
								if (listItm instanceof Map) {
									listItmObj = MapUtil.mapToObject((Map<String, Object>) listItm, listGenTypClz, printLog);
								} else {
									listItmObj = listItm;
								}
								listObj.add(listItmObj);
							}
							field.set(obj, listObj);
							break;
						}
					}
				}
			} else if (mapItem instanceof Map) {
				field.set(obj, MapUtil.mapToObject((Map<String, Object>) mapItem, fieldClass, printLog));
			} else {
				field.set(obj, mapItem);
			}
		}
		if (printLog)
			logger.debug("------mapToObject end------" + beanClass.getName());
		return obj;
	}
	
	/**
	 * 将对象转为Map
	 * @param obj
	 * @return
	 * @throws Exception 
	 */
	public static Map<String, Object> objectToMap(Object obj, List<String> includeFields) throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		BeanInfo beanInfo = Introspector.getBeanInfo(obj.getClass());
		PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
		for (PropertyDescriptor property : propertyDescriptors) {
			String propName = property.getName();
			if (propName.equals("class"))
				continue;
			
			if (includeFields != null) {
				if (!includeFields.contains(propName))
					continue;
			}
			
			Method getter = property.getReadMethod();
			
			Object propVal = getter.invoke(obj);
			
			resultMap.put(propName, propVal);
		}
		return resultMap;
	}
	
	/**
	 * 将Map值从Object转成String
	 * @param map
	 * @return
	 * @throws Exception 
	 */
	public static Map<String, String> objMapToStrMap(Map<String, Object> map) throws Exception {
		Map<String, String> newMap = new HashMap<String, String>();
		for (String key : map.keySet()) {
			String val = map.get(key) == null ? "" : map.get(key).toString();
			newMap.put(key, val);
		}
		return newMap;
	}
	
	/**
	 * to lower case key map
	 * @param point
	 * @return
	 */
	public static Map<String, Object> toLowerCaseKeyMap(Map<String, Object> point) {
		Map<String, Object> lowCaseKeyMap = new HashMap<String, Object>();
		for (String key : point.keySet()) {
			lowCaseKeyMap.put(key.toLowerCase(), point.get(key));
		}
		return lowCaseKeyMap;
	}
	
	/**
	 * JsonString2Map
	 * @param jsonStr
	 * @return
	 */
	public static Map<String, Object> parseJSON2Map(String jsonStr) {
		logger.debug("parseJSON2Map++++++++++++");
		
		Map<String, Object> map = new HashMap<String, Object>();
		
		JSONObject json = JSONObject.parseObject(jsonStr);
		for (Object k : json.keySet()) {
			logger.debug("Key : " + k);
			Object v = json.get(k);
			
			if (v instanceof JSONArray) {
				List<Object> list = new ArrayList<Object>();
				
				for (Object o : (JSONArray) v) {
					if (o instanceof String) {
						logger.debug("String item in array : " + o.toString());
						list.add(o);
					} else {
						list.add(parseJSON2Map(o.toString()));
					}
				}
				map.put(k.toString(), list);
			} else {
				map.put(k.toString(), v);
			}
		}
		logger.debug("parseJSON2Map---------");
		return map;
	}
}
