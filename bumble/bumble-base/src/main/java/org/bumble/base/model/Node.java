package org.bumble.base.model;

import org.bumble.base.model.URL;

public class Node extends URL {
	public Node() {}
	public Node(String name, URL url) {
		super(url.getUrl());
		this.name = name;
	}
	
	private String name = null;
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	private Long lastAvailableTimestamp;
	
	public Long getLastAvailableTimestamp() {
		return lastAvailableTimestamp;
	}
	
	public void setLastAvailableTimestamp(Long lastAvailableTimestamp) {
		this.lastAvailableTimestamp = lastAvailableTimestamp;
	}
}
