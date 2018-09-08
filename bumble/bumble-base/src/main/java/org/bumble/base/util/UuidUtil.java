package org.bumble.base.util;

import java.util.UUID;

public class UuidUtil {
	public static String uuid() {
		String uuid = UUID.randomUUID().toString().replaceAll("-", "");
		return uuid;
	}
}
