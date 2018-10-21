package org.bumble.manager.test;

import java.util.concurrent.ExecutorService;

import org.bumble.core.action.Action;
import org.bumble.core.action.ActionConst;
import org.bumble.core.remoting.client.IRemotingTransporterClient;
import org.bumble.core.remoting.client.RemotingTransporterClientFactory;
import org.bumble.core.remoting.server.RemotingTransporterServer;
import org.bumble.core.thread.ThreadExecutorGenerator;

public class MySocketTest {
	public static void main( String[] args ) throws Exception
    {

		ExecutorService threadPool = ThreadExecutorGenerator.getInstance().getExecutor();
		
		RemotingTransporterServer transporterServer = new RemotingTransporterServer(threadPool, "xxx", 8086);
		transporterServer.start();
		
		final IRemotingTransporterClient client = RemotingTransporterClientFactory.getInstance().getClient("yyy", "10.18.5.110", 8086);
		
		client.enableHeart(false);
		client.start();
		
		Action heartAction = new Action(ActionConst.Type.HEART_REQ);
		String msg = heartAction.toJsonString();
		
		client.sendMsg(msg);
		client.sendMsg(msg);
		
		System.out.println("Msg sent");
    }
}
