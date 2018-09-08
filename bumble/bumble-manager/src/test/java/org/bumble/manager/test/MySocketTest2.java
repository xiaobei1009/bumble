package org.bumble.manager.test;

import org.bumble.base.log.LogInitializer;
import org.bumble.core.action.Action;
import org.bumble.core.action.ActionConst;
import org.bumble.core.remoting.client.IRemotingTransporterClient;
import org.bumble.core.remoting.client.RemotingTransporterClientFactory;

public class MySocketTest2 {

	static LogInitializer li = new LogInitializer();
	
	public static void main( String[] args ) throws Exception
    {
		li.init();
		
		IRemotingTransporterClient client = RemotingTransporterClientFactory.getInstance().getClient("xxx", "10.18.5.110", 8088);
		
		client.enableHeart(false);
		client.start();
		
		Action heartAction = new Action(ActionConst.Type.HEART_REQ);
		String heartJsonStr = heartAction.toJsonString();
		
		client.sendMsg(heartJsonStr);
    }
}
