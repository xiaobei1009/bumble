package org.bumble.core.action.impl.mngr;

import java.nio.channels.SocketChannel;

import org.bumble.core.action.Action;
import org.bumble.core.action.ActionService;
import org.bumble.manager.BumbleManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Receive Shutdown Message from bumble manager itself, then process exit
 * <p>
 * @author shenxiangyu
 *
 */
public class ShutdownActionService implements ActionService {
	
	protected Logger logger = LoggerFactory.getLogger(this.getClass());
	
	public void execute(Action action, SocketChannel channel, Object... param) {
		
		try {
			BumbleManager.getInstance().shutdown();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		
	}
}
