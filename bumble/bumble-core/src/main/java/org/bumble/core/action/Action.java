package org.bumble.core.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bumble.base.BasicConst;
import org.bumble.base.BumbleNameBuilder;
import org.bumble.base.util.MapUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;

public class Action {
	
	private static Logger logger = LoggerFactory.getLogger(Action.class);
	
	private String name = null;
	private Map<String, Object> param = new HashMap<String, Object>();
	
	// FILO
	//private Stack<String> callstack = new Stack<String>();
	private List<String> callstack = new ArrayList<String>();
	
	public Action(String name) {
		this.setName(name);
	}
	
	public void addCallstack() {
		String uniqName = BumbleNameBuilder.getInstance().getUniqName();
		callstack.add(uniqName);
	}
	
	public void addCallstack(String uniqName) {
		callstack.add(uniqName);
	}
	
	public String getRespDestination() {
		int index = callstack.size() - 1;
		String uniqName = callstack.get(index);
		callstack.remove(index);
		return uniqName;
	}
	
	public Action(String name, Map<String, Object> param) {
		this.setName(name);
		this.setParam(param);
	}
	
	public String toJsonString() {
		JSONObject jsnObj = new JSONObject();
		jsnObj.put(ActionConst.NAME, name);
		String callstackStr = String.join(BasicConst.COMMA, callstack);
		jsnObj.put(ActionConst.CALLSTACK, callstackStr);
		
		if (param != null) {
			JSONObject paramJsnObj = new JSONObject();
			for (String key : param.keySet()) {
				paramJsnObj.put(key, param.get(key));
			}
			jsnObj.put(ActionConst.PARAM, paramJsnObj);
		}
		String ret = jsnObj.toJSONString();
		return ret;
	}
	
	public static Action parseJson(String json) {
		try {
			JSONObject jsnObj = (JSONObject) JSONObject.parse(json);
			String actionName = jsnObj.getString(ActionConst.NAME);
			String callstackStr = jsnObj.getString(ActionConst.CALLSTACK);
			
			Map<String, Object> actionParamMap = null;
			if (jsnObj.containsKey(ActionConst.PARAM)) {
				String actionParamStr = jsnObj.get(ActionConst.PARAM).toString();
				actionParamMap = MapUtil.parseJSON2Map(actionParamStr);
			}
			Action action = new Action(actionName, actionParamMap);

			for (String uniqName : callstackStr.split(BasicConst.COMMA)) {
				action.addCallstack(uniqName);
			}
			
			return action;
		} catch (Exception e) {
			logger.error("Parse json failed: " + json);
			logger.trace(e.getMessage(), e);
		}
		
		return null;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Map<String, Object> getParam() {
		return param;
	}

	public void setParam(Map<String, Object> param) {
		this.param = param;
	}
	
	public void setParamEntry(String key, Object value) {
		this.param.put(key, value);
	}
	
	public void deleteParamEntry(String key) {
		this.param.remove(key);
	}
	
	public String getParamEntry(String key) {
		return (String) this.param.get(key);
	}
}
