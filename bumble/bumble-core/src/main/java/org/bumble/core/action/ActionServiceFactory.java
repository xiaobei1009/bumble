package org.bumble.core.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bumble.base.BasicConst;
import org.bumble.base.util.ReflectUtil;
import org.bumble.core.action.impl.NoopActionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Get action service by the action name
 * E.g. If the action name is connect-success and the class name will be calculated as 
 * ConnectSuccessActionService, and the code will lookup the classpath and find
 * <p>
 * @author shenxiangyu
 *
 */
public class ActionServiceFactory {
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	private static ActionServiceFactory instance = null;
	
	public static ActionServiceFactory getInstance() {
		if (instance == null) {
			instance = new ActionServiceFactory();
		}
		return instance;
	}
	
	private String capitalUpperCase(String str) {  
	    return str.substring(0, 1).toUpperCase() + str.substring(1);  
	}  
	
	private Map<String, ActionService> serviceMap = new HashMap<String, ActionService>();
	
	@SuppressWarnings("rawtypes")
	public ActionService getService(Action action) {
		if (action == null || action.getName() == null) {
			return new NoopActionService();
		}
		String[] actionTypeArr = action.getName().split(BasicConst.HYPHEN);
		String className = "";
		for (String word : actionTypeArr) {
			className += capitalUpperCase(word);
		}
		className += "ActionService";
		ActionService actionService = null;
		
		if (serviceMap.containsKey(className)) {
			actionService = serviceMap.get(className);
		} else {
			try {
				List<Class> classes = ReflectUtil.getImplementClasses4Interface(ActionService.class);
				for (Class clazz : classes) {
					if (clazz.getSimpleName().equals(className)) {
						actionService = (ActionService) clazz.newInstance();
						serviceMap.put(className, actionService);
						break;
					}
				}
			} catch (Exception e) {
				logger.error(e.getMessage());
				logger.trace(e.getMessage(), e);
			}
		}
		
		return actionService;
	}
}
