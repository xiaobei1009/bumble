package org.bumble.core.action;

import java.nio.channels.SocketChannel;

/**
 * Do action according to the coming socket message
 * 
 * @author shenxiangyu
 *
 */
public interface ActionService {
	
	/**
	 * Do action
	 * <p>
	 * @param action
	 * @param channel
	 * @param param
	 */
	void execute(Action action, SocketChannel channel, Object... param);
}
