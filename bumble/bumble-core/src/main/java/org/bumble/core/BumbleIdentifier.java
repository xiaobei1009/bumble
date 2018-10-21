package org.bumble.core;

import org.bumble.base.config.LocalConfigConst;
import org.bumble.base.config.LocalConfigHolder;

public class BumbleIdentifier {
	private static BumbleIdentifier instance = new BumbleIdentifier();
	public BumbleIdentifier() {
		String mngrPort = LocalConfigHolder.getInstance().getConfig(LocalConfigConst.BumbleManagerServer.PORT);
        if (mngrPort != null) {
        	isManager = true;
        	isClient = false;
        } else {
        	isManager = false;
        	isClient = true;
        }
	}
	
	public static BumbleIdentifier getInstance() {
		return instance;
	}
	private Boolean isManager = null;
	private Boolean isClient = null;
	
	public Boolean isClient() {
		return isClient;
	}
	public Boolean isManager() {
		return isManager;
	}
}
