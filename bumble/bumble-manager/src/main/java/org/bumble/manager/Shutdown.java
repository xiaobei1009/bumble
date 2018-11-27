package org.bumble.manager;

import java.net.InetAddress;

import org.bumble.base.BumbleNameBuilder;
import org.bumble.base.config.LocalConfigConst;
import org.bumble.base.config.LocalConfigHolder;
import org.bumble.base.log.LogInitializer;
import org.bumble.core.action.Action;
import org.bumble.core.action.ActionConst;
import org.bumble.core.remoting.client.IRemotingTransporterClient;
import org.bumble.core.remoting.client.RemotingTransporterClientFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// TODO try to terminate the zk connection, test on sh invocation
public class Shutdown {
	
	public static void main(String[] args) throws Exception {
		
		System.out.println("Closing...");
		
		LocalConfigHolder.getInstance().overrideByArgs(args);
		
		LogInitializer logInitializer = new LogInitializer();
    	logInitializer.init();
		
		Logger logger = LoggerFactory.getLogger(Shutdown.class);
		
		String managerName = BumbleNameBuilder.getInstance().getName();
		String ip = null;
		try {
			InetAddress address = InetAddress.getLocalHost();
			ip = address.getHostAddress();
		} catch (Exception e) {
			logger.error(e.getMessage());
			logger.trace(e.getMessage(), e);
		}
		
		String managerPortStr = LocalConfigHolder.getInstance().getConfig(LocalConfigConst.BumbleManagerServer.PORT);
		int port = Integer.valueOf(managerPortStr);
		
		IRemotingTransporterClient client = RemotingTransporterClientFactory.getInstance().getClient(managerName, ip, port);
		client.enableHeart(false);
		client.start();
		
		Action shutdownAction = new Action(ActionConst.Type.SHUTDOWN);
		String shutdownMsg = shutdownAction.toJsonString();
		
		client.sendMsg(shutdownMsg);
		
		client.close();
	}
}
