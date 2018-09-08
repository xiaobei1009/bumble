package org.bumble.base.model;

public class URL {
	
	public URL() {}
	
	public URL(String url) {
		setUrl(url);
	}
	
	public URL(String ip, String port) {
		this.ip = ip;
		this.port = port;
		setUrl(ip + ":" + port);
	}
	
	private String ip = "";
	private String port = "";
	private String url = "";
	
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		setUrl(ip + ":" + port);
	}
	public String getPort() {
		return port;
	}
	public void setPort(String port) {
		setUrl(ip + ":" + port);
	}
	
	public void setUrl(String url) {
		String[] data = url.split(":");
		switch (data.length) {
			case 1:
				ip = data[0];
				break;
			case 2:
				ip = data[0];
				port = data[1];
				break;
		}
		this.url = url;
	}
	
	public String getUrl() {
		return url;
	}
}
