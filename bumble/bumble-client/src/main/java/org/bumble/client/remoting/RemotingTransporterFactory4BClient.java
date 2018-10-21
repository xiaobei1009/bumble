package org.bumble.client.remoting;

import java.net.InetAddress;
import java.util.Iterator;

import org.bumble.base.BumbleNameBuilder;
import org.bumble.base.Callback;
import org.bumble.core.BumbleLogoPrinter;
import org.bumble.core.remoting.AbstractRemotingTransporterFactory;
import org.bumble.core.remoting.client.IRemotingTransporterClient;
import org.bumble.core.remoting.client.RemotingTransporterClientFactory;

import org.bumble.registry.RegistryFactory;
import org.bumble.registry.data.MngrNode;
import org.bumble.registry.data.RegistryData;

public class RemotingTransporterFactory4BClient 
	extends AbstractRemotingTransporterFactory 
	implements IRemotingTransporterFactory4BClient {
	
	private volatile static RemotingTransporterFactory4BClient instance = null;
	
	public static RemotingTransporterFactory4BClient getInstance() {
		if (instance == null) {
			instance = new RemotingTransporterFactory4BClient();
		}
		return instance;
	}
	
	public void sendMsg(String msg) {
		client.sendMsg(msg);
	}
	
	public RemotingTransporterFactory4BClient() {
		super();
		this.uniqName = BumbleNameBuilder.getInstance().getUniqName();
		try {
			InetAddress address = InetAddress.getLocalHost();
			this.ip = address.getHostAddress();
		} catch (Exception e) {
			logger.error(e.getMessage());
			logger.trace(e.getMessage(), e);
		}
	}
	
	private IRemotingTransporterClient client = null;
	
	private String ip = null;
	private String managerUniqName = null;
	
	public String getUniqName() {
		return this.uniqName;
	}
	
	@Override
	protected void doStart() throws Exception {
		connectToManager();
	}
	
	private Boolean isConnected = false;
	
	@Override
	protected void doClose() {}
	
	public void connectToManager() {
		if (isConnected)
			return;
		
		while (true) {
			// Get bumble transaction manager from registry
			final MngrNode mngrNode = RegistryFactory.getRegistry().getMngr4Client(closedClientNameList);
			
			try {
				if (mngrNode == null) {
					logger.warn("There is no Bumble Manager in the registry.");
					Thread.sleep(5000);
					continue;
				}
				client = RemotingTransporterClientFactory.getInstance().getClient(mngrNode.getName(), mngrNode.getIp(), Integer.valueOf(mngrNode.getPort()));
				
				logger.info("Try to connect to Bumble Manager[" + mngrNode.getUrl() + "]");
				
				client.start();
				if (client.isClosed()) {
					handleUnhealthyTransporter(client);
					return;
				}
				transporterClientMap.clear();
				managerUniqName = null;
				transporterClientMap.put(mngrNode.getName(), client);
				
				RegistryFactory.getRegistry().bindClientToManager(this.uniqName, this.ip, mngrNode.getName());
				
				isConnected = true;
			} catch (Exception e) {
				logger.error(e.getMessage());
				logger.trace(e.getMessage(), e);
			}
			if (isConnected) {
				managerUniqName = mngrNode.getName();
				BumbleLogoPrinter.print(new Callback() {
					public Object doCallback(Object... params) {
						String logoStr = (String) params[0];
						logoStr = logoStr.replace("{{managerName}}", mngrNode.getName());
						return logoStr;
					}
				});
				break;
			}
		}
	}

	@Override
	protected void doHandleUnhealthyTransporter(IRemotingTransporterClient transporter) {
		String name = transporter.getName();
		logger.info("Bumble Manager[" + name + "] get lost, Try to get a new one to connect.");
		
		closedClientNameList.add(name);
		isConnected = false;
		
		// If failed to connect this Bumble Manager, then get a new Bumble Manager to connect
		connectToManager();
	}

	public void doAfterRegistryChange(RegistryData registryData) {
		if (closedClientNameList.isEmpty()) {
			return;
		}
		for (Iterator<String> it = closedClientNameList.iterator(); it.hasNext();) {
			String name = it.next();
			if (!registryData.getManagerNameList().contains(name)) {
				it.remove();
			}
		}
	}

	@Override
	protected void doHealthCheck(Long heartBeatTimeout) {
		
	}

	public Boolean isConnected() {
		return isConnected;
	}

	public String getManagerUniqName() {
		return managerUniqName;
	}
	
}
