package org.bumble.manager.test;

import java.util.concurrent.ExecutorService;

import org.bumble.base.log.LogInitializer;
import org.bumble.core.action.Action;
import org.bumble.core.action.ActionConst;
import org.bumble.core.remoting.client.IRemotingTransporterClient;
import org.bumble.core.remoting.client.RemotingTransporterClientFactory;
import org.bumble.core.remoting.server.RemotingTransporterServer;
import org.bumble.core.thread.ThreadExecutorGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MySocketTest3 {
	
	static LogInitializer li = new LogInitializer();
	private static Logger logger = LoggerFactory.getLogger(MySocketTest3.class);
	
	public static void main( String[] args ) throws Exception
    {
		li.init();
		
		final ExecutorService threadPool = ThreadExecutorGenerator.getInstance().getExecutor();
		
		Action heartAction = new Action(ActionConst.Type.HEART_REQ);
		String msg = heartAction.toJsonString();
		
		
		logger.info("Start Server 8086");
		RemotingTransporterServer s8086 = new RemotingTransporterServer(threadPool, "xxx", 8086);
		s8086.start();
		Thread.sleep(5000);
		logger.info("5 Second... \n");
		
		logger.info("Send to 8088 [1]");
		IRemotingTransporterClient c8088 = RemotingTransporterClientFactory.getInstance().getClient("yyy", "10.18.5.110", 8088);
		c8088.enableHeart(false);
		c8088.start();
		c8088.sendMsg(msg);
		Thread.sleep(5000);
		logger.info("5 Second... \n");
		
		logger.info("Send to 8086");
		IRemotingTransporterClient c8086 = RemotingTransporterClientFactory.getInstance().getClient("zzz", "10.18.5.110", 8086);
		c8086.enableHeart(false);
		c8086.start();
		c8086.sendMsg(msg);
		Thread.sleep(5000);
		logger.info("5 Second... \n");
		
		logger.info("Send to 8088 [2]");
		c8088.sendMsg(msg);
		
		while(true) {
			c8086.sendMsg(msg);
			c8088.sendMsg(msg);
			Thread.sleep(5000);
		}
    }
}
