package org.bumble.base.util;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NioSocketUtil {
	
	private static Logger logger = LoggerFactory.getLogger(NioSocketUtil.class);
	
	public static void sendMsg(SocketChannel channel, String msg) throws IOException {
		logger.debug("Send " + msg);
		msg += "\n";
		channel.write(ByteBuffer.wrap(msg.getBytes()));
	}
}
