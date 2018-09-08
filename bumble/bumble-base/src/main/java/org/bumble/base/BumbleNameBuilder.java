package org.bumble.base;

import org.bumble.base.config.LocalConfigConst;
import org.bumble.base.config.LocalConfigHolder;
import org.bumble.base.util.UuidUtil;

public class BumbleNameBuilder {
	private static BumbleNameBuilder instance = new BumbleNameBuilder();
	public static BumbleNameBuilder getInstance() {
		return instance;
	}
	public String getName() {
		if (name == null) {
			name = LocalConfigHolder.getInstance().getConfig(LocalConfigConst.Basic.NAME);
		}
		return name;
	}
	
	private String name = null;
	private String uniqName = null;
	
	public String getUniqName() {
		if (uniqName != null) {
			return uniqName;
		}
		if (uniqName == null) {
			uniqName = LocalConfigHolder.getInstance().getConfig(LocalConfigConst.Basic.UNIQ_NAME);
		}
		if (uniqName == null) {
			String _name = getName();
			String uuid = UuidUtil.uuid();
			if (_name.length() >= 23) {
				uniqName = uuid;
			} else {
				uniqName = _name + "-" + uuid.substring(_name.length() + 1);
			}
			LocalConfigHolder.getInstance().setConfig(LocalConfigConst.Basic.UNIQ_NAME, uniqName);
		}
		return uniqName;
	}
}
