package org.bumble.test.model;

import java.util.List;
import java.util.Map;

public class DeepCopyInner {
	private String hello;
	private Map<String, String> helloMap;
	private List<String> helloList;
	// TODO other type...
	
	public String getHello() {
		return hello;
	}
	public void setHello(String hello) {
		this.hello = hello;
	}
	public Map<String, String> getHelloMap() {
		return helloMap;
	}
	public void setHelloMap(Map<String, String> helloMap) {
		this.helloMap = helloMap;
	}
	public List<String> getHelloList() {
		return helloList;
	}
	public void setHelloList(List<String> helloList) {
		this.helloList = helloList;
	}
}
