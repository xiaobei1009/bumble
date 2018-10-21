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

public class MySocketTest4 {
	
	static LogInitializer li = new LogInitializer();
	private static Logger logger = LoggerFactory.getLogger(MySocketTest4.class);
	
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
		
		logger.info("Send to 8088");
		IRemotingTransporterClient c8088 = RemotingTransporterClientFactory.getInstance().getClient("yyy", "10.18.5.110", 8088);
		c8088.enableHeart(false);
		c8088.start();
		c8088.sendMsg(msg);
		Thread.sleep(5000);
		logger.info("5 Second... \n");
		
		IRemotingTransporterClient c8090 = null;
		int i = 0;
		while(true) {
			c8088.sendMsg(msg);
			
			if (i == 5) {
				c8090 = RemotingTransporterClientFactory.getInstance().getClient("zzz" + i, "10.18.5.110", 8090);
				c8090.enableHeart(false);
				c8090.start();
			}
			if (i > 5) {
				c8090.sendMsg(msg);
			}
			i++;
			c8088.sendMsg(msg);
			Thread.sleep(5000);
		}
    }
}
