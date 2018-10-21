package org.bumble.core.action.impl;

import java.nio.channels.SocketChannel;

import org.bumble.core.action.Action;
import org.bumble.core.action.ActionService;
import org.bumble.core.remoting.client.IRemotingTransporterClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Receive Heart Response Message from bumble manager
 * <p>
 * This can be bumble manager or bumble client
 * This is executed by the socket client
 * <p>
 * @author shenxiangyu
 *
 */
public class HeartRespActionService implements ActionService {
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	public void execute(Action action, SocketChannel channel, Object... param) {
		try {
			IRemotingTransporterClient client = (IRemotingTransporterClient) param[0];
			
			String remoteUrl = channel.getRemoteAddress().toString().substring(1);
			
    		logger.debug("ReceiveHeartResponse from [" + remoteUrl + "]");
    		
    		Long currTimestamp = System.currentTimeMillis();
    		
    		logger.debug("Set last available time stamp for bumble manager [" + remoteUrl + "]: " + currTimestamp);
    		
    		// Refresh the last available time stamp for the corresponding server.
    		client.setLastAvailableTimestamp(currTimestamp);
		} catch(Exception e) {
			logger.error(e.getMessage());
			logger.trace(e.getMessage(), e);
		}
	}
	
}
