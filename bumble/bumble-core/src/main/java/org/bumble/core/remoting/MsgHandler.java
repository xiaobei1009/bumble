package org.bumble.core.remoting;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

import org.bumble.base.Callback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MsgHandler {
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	public void handle(SocketChannel channel, Callback callback) throws IOException {
    	ByteBuffer buffer = null;
    	byte[] data = null;
        String msgFragment = "";
        
        while (true) {
        	buffer = ByteBuffer.allocate(10);
            int read = channel.read(buffer);
            data = buffer.array();
            msgFragment += new String(data);
            
            if (read <= 0)
            	break;
        }
        logger.debug("handle: " + msgFragment);
        
        if (msgFragment.contains("\n")) {
			String[] msgFragmentArr = msgFragment.split("\n");
			msgFragmentArr[0] = msgRemaining + msgFragmentArr[0];
			
			for (int i = 0; i < msgFragmentArr.length - 1; i++) {
				// deal with message
				callback.doCallback(msgFragmentArr[i]);
			}
			msgRemaining = msgFragmentArr[msgFragmentArr.length - 1];
			msgRemaining = msgRemaining.replaceAll("\0", "");
		} else {
			msgRemaining += msgFragment;
		}
	}
	
	private String msgRemaining = "";
}
