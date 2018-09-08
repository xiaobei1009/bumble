package org.bumble.registry.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bumble.base.model.Node;

import com.alibaba.fastjson.JSONObject;

public class RegistryData {
	// Key is manager name, value is manager model
	private Map<String, MngrNode> mngrs = new HashMap<String, MngrNode>();
	
	// Client name list
	private List<String> clientNameList = new ArrayList<String>();
	
	// Manager name list
	private List<String> managerNameList = new ArrayList<String>();
	
	// Key is client name, value is manager name
	private Map<String, String> clientMngrMap = new HashMap<String, String>();
	
	public List<String> getClientNameList() {
		return clientNameList;
	}
	
	public Map<String, String> getClientMngrMap() {
		return clientMngrMap;
	}
	
	public List<String> getManagerNameList() {
		return managerNameList;
	}
	
	public Map<String, MngrNode> getManagers() {
		return mngrs;
	}
	
	public void addMngrNode(MngrNode mngrNode) {
		String name = mngrNode.getName();
		if (!mngrs.containsKey(name)) {
			mngrs.put(name, mngrNode);
			for (String clientName : mngrNode.getClientNameList()) {
				clientMngrMap.put(clientName, name);
				if (!clientNameList.contains(clientName)) {
					clientNameList.add(clientName);	
				}
			}
		}
		if (!managerNameList.contains(name)) {
			managerNameList.add(name);
		}
	}
	
	public void removeMngrNode(String name) {
		MngrNode mngrNode = mngrs.get(name);
		for (String clientName : mngrNode.getClientNameList()) {
			clientNameList.remove(clientName);
			clientMngrMap.remove(clientName);
		}
		mngrs.remove(name);
		managerNameList.remove(name);
	}
	
	public void removeClient(String name) {
		String mngrName = clientMngrMap.get(name);
		MngrNode mngrNode = mngrs.get(mngrName);
		mngrNode.removeClient(name);
		clientNameList.remove(name);
		clientMngrMap.remove(name);
	}
	
	public void addClient2Mngr(Node clientNode, String mngrName) {
		MngrNode mngrNode = mngrs.get(mngrName);
		if (mngrNode == null) {
			return;
		}
		mngrNode.addClient(clientNode);
		String clientName = clientNode.getName();
		if (!clientNameList.contains(clientName)) {
			clientNameList.add(clientName);
		}
		if (!clientMngrMap.containsKey(clientName)) {
			clientMngrMap.put(clientName, mngrName);
		}
	}
	
	public JSONObject toJsonObj() {
		JSONObject jsonObj = new JSONObject();
		
		JSONObject mngrsJsonObj = new JSONObject();
		for (String name : mngrs.keySet()) {
			MngrNode mngrNode = mngrs.get(name);
			JSONObject mngrNodeJsonObj = mngrNode.toJsonObj();
			mngrsJsonObj.put(name, mngrNodeJsonObj);
		}
		
		jsonObj.put("managers", mngrsJsonObj);
		return jsonObj;
	}
	
	public String toJsonString() {
		JSONObject jsonObj = toJsonObj();
		return jsonObj.toJSONString();
	}
	
	public void parseJsonString(String jsonStr) {
		JSONObject jsonObj = (JSONObject) JSONObject.parse(jsonStr);
		JSONObject mngsJsonObj = jsonObj.getJSONObject("managers");
		mngrs = new HashMap<String, MngrNode>();
		managerNameList = new ArrayList<String>();
		clientNameList = new ArrayList<String>();
		clientMngrMap = new HashMap<String, String>();
		
		for (String mngrName : mngsJsonObj.keySet()) {
			JSONObject mngrJsonObj = (JSONObject) mngsJsonObj.get(mngrName);
			MngrNode mngrNode = new MngrNode();
			mngrNode.parseJsonString(mngrJsonObj.toJSONString());
			if (!managerNameList.contains(mngrName)) {
				managerNameList.add(mngrName);
			}
			if (!mngrs.containsKey(mngrName)) {
				mngrs.put(mngrName, mngrNode);
			}
			for (String clientName : mngrNode.getClientNameList()) {
				if (!clientNameList.contains(clientName)) {
					clientNameList.add(clientName);
				}
				if (!clientMngrMap.containsKey(clientName)) {
					clientMngrMap.put(clientName, mngrName);
				}
			}
		}
		
	}
}
