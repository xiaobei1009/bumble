package org.bumble.core.action.impl.mngr;

import java.nio.channels.SocketChannel;

import org.bumble.base.util.NioSocketUtil;
import org.bumble.core.action.Action;
import org.bumble.core.action.ActionConst;
import org.bumble.core.action.ActionService;
import org.bumble.core.remoting.server.IRemotingTransporterServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Receive Heart Request Message from brother manager or client
 * And Send Heart Response
 * <p>
 * @author shenxiangyu
 *
 */
public class HeartReqActionService implements ActionService {
	
	protected Logger logger = LoggerFactory.getLogger(this.getClass());
	
	public void execute(Action action, SocketChannel channel, Object... param) {
		
		// Build Heart Response
		Action heartRespAction = new Action(ActionConst.Type.HEART_RESP);
		
		IRemotingTransporterServer server = (IRemotingTransporterServer) param[0];
		String reqFromUniqName = action.getParamEntry(ActionConst.Param.REQ_FROM_UNIQ_NAME);
		
		// Update connected bumble client last available time stamp
		server.updateLastAvailableTimestamp4ClientConnected(reqFromUniqName);
		logger.debug("Send heart response to client: " + reqFromUniqName);
		
		// Send Heart Response
		String actionStr = heartRespAction.toJsonString();
		try {
			NioSocketUtil.sendMsg(channel, actionStr);
		} catch (Exception e) {
			logger.warn("Failed to send heart beat to [" + reqFromUniqName + "]");
		}
	}
}
