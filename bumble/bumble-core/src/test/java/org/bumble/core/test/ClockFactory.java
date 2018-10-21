package org.bumble.core.test;

import java.util.ArrayList;
import java.util.List;

public class ClockFactory {
	private List<Clock> clocks = new ArrayList<Clock>();
	public void addClock(Clock clock) {
		clocks.add(clock);
	}
	public String toString() {
		String ret = "";
		for (Clock c : clocks) {
			ret += c.getTime().toString();
		}
		return ret;
	}
}
