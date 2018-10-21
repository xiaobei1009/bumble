package org.bumble.core.remoting;

import java.util.concurrent.ExecutorService;

import org.bumble.core.thread.ThreadExecutorGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RemotingTransporter {
	
	protected Logger logger = LoggerFactory.getLogger(this.getClass());
	
	protected int idleTimeout = 0;
	protected ExecutorService threadPool = null;
	
	/**
	 * The meanings of name for client and server are different<br>
	 * RemotingTransporterClient: stands for the remote server bumble unique name to be connected<br>
	 * RemotingTransporterServer: stands for the local server bumble unique name<br>
	 */
	protected String name = null;
	
	protected MsgHandler msgHandler = new MsgHandler();
	
	public RemotingTransporter(ExecutorService threadPool, String name) {
		this.threadPool = threadPool;
		this.name = name;
	}
	
	/**
	 * Used for testing
	 */
	@Deprecated
	public RemotingTransporter() {
		this.threadPool = ThreadExecutorGenerator.getInstance().getExecutor();
	}
}
