package org.bumble.core.action.impl.mngr;

import java.nio.channels.SocketChannel;

import org.bumble.core.BumbleConst;
import org.bumble.core.action.Action;
import org.bumble.core.action.ActionService;
import org.bumble.core.remoting.server.IRemotingTransporterServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Bumble client connect to bumble manager successfully
 * then bumble manager record the socket channel of the client
 * <p>
 * @author shenxiangyu
 *
 */
public class ConnectSuccessActionService implements ActionService {
	
	protected Logger logger = LoggerFactory.getLogger(this.getClass());
	
	public void execute(Action action, SocketChannel channel, Object... param) {
		String clientUniqName = (String) action.getParam().get(BumbleConst.CLIENT_UNIQ_NAME);
		IRemotingTransporterServer server = (IRemotingTransporterServer) param[0];
		
		logger.debug("Update client channel for [" + clientUniqName + "]");
		server.updateClientChannel(clientUniqName, channel);
	}
}