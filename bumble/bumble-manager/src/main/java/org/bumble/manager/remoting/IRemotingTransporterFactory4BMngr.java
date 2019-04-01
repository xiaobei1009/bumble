package org.bumble.manager.remoting;

import org.bumble.core.action.Action;

public interface IRemotingTransporterFactory4BMngr {
	/**
	 * Send message to a brother bumble manager
	 * <p>
	 * @param mngrName
	 * @param action
	 */
	void sendMsgToMngr(String mngrName, Action action);
	
	/**
	 * Send message to a client
	 * <p>
	 * @param clientName
	 */
	void sendMsgToClient(String clientName, Action action);
	
	/**
	 * Send message to client or manager due to the uniqName
	 * <p>
	 * @param uniqName
	 * @param action
	 */
	void sendMsg(String uniqName, Action action);
}
