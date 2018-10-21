package org.bumble.core.test;

public class Clock {
	private Long time = (long) 0;
	private Boolean bool = false;
	public void setTime(Long time) {
		this.time = time;
	}
	public Long getTime() {
		return this.time;
	}
	public Boolean getBool() {
		return bool;
	}
	public void setBool(Boolean bool) {
		this.bool = bool;
	}
}