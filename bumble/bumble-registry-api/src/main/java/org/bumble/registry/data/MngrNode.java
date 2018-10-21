package org.bumble.registry.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bumble.base.model.Node;
import org.bumble.base.model.URL;

import com.alibaba.fastjson.JSONObject;

public class MngrNode extends Node {
	public MngrNode() {}
	public MngrNode(String name, URL url) {
		super(name, url);
	}
	
	// Client name list
	private List<String> clientNameList = new ArrayList<String>();
	
	// Key is client name, value is client model
	private Map<String, Node> clients = new HashMap<String, Node>();
	
	public void addClient(Node node) {
		String name = node.getName();
		if (!clientNameList.contains(name)) {
			clientNameList.add(name);
		}
		clients.put(name, node);
	}
	
	public void removeClient(String name) {
		clientNameList.remove(name);
		clients.remove(name);
	}
	
	public List<String> getClientNameList() {
		return clientNameList;
	}
	
	public Map<String, Node> getClients() {
		return clients;
	}
	
	public JSONObject toJsonObj() {
		JSONObject clientsJsonObj = new JSONObject();
		for (String clientName : clients.keySet()) {
			String clientUrl = clients.get(clientName).getUrl();
			
			JSONObject clientJsonObj = new JSONObject();
			clientJsonObj.put(NAME, clientName);
			clientJsonObj.put(URL, clientUrl);
			clientsJsonObj.put(clientName, clientJsonObj);
		}
		JSONObject jsonObj = new JSONObject();
		jsonObj.put(NAME, getName());
		jsonObj.put(URL, getUrl());
		jsonObj.put(CLIENTS, clientsJsonObj);
		return jsonObj;
	}
	
	public String toJsonString() {
		JSONObject jsonObj = toJsonObj();
		return jsonObj.toJSONString();
	}
	
	private static final String NAME = "name";
	private static final String URL = "url";
	private static final String CLIENTS = "clients";
	
	public void parseJsonString(String jsonStr) {
		JSONObject jsonObj = (JSONObject) JSONObject.parse(jsonStr);
		String name = (String) jsonObj.get(NAME);
		String url = (String) jsonObj.get(URL);
		this.clientNameList = new ArrayList<String>();
		this.clients = new HashMap<String, Node>();
		this.setName(name);
		this.setUrl(url);
		JSONObject clients = (JSONObject) jsonObj.get(CLIENTS);
		for (String clientName : clients.keySet()) {
			JSONObject serviceJsonObj = (JSONObject) clients.get(clientName);
			String clientUrl = (String) serviceJsonObj.get(URL);
			this.addClient(new Node(clientName, new URL(clientUrl)));
		}
	}
}
