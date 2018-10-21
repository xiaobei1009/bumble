package org.bumble.core.remoting.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;

import org.bumble.base.Callback;
import org.bumble.base.model.Node;
import org.bumble.base.model.URL;
import org.bumble.base.util.NioSocketUtil;
import org.bumble.core.WorkNeedToShutdown;
import org.bumble.core.action.Action;
import org.bumble.core.action.ActionService;
import org.bumble.core.action.ActionServiceFactory;
import org.bumble.core.remoting.MsgHandler;
import org.bumble.core.remoting.RemotingTransporter;


public class RemotingTransporterServer extends RemotingTransporter 
	implements IRemotingTransporterServer, WorkNeedToShutdown {
	
    private Boolean isStarting = false;
    private Boolean isStarted = false;
    
    private int port = 0;
    
    private MsgHandler msgHandler = new MsgHandler();
    
    // If the server get heart beat request from a bumble client 
    // Then the client url(with actual port) will be recorded to this hash map
    private Map<String, Node> clientsConnectedToServer = new ConcurrentHashMap<String, Node>();
	
	public RemotingTransporterServer(ExecutorService threadPool, String name, int port) {
		super(threadPool, name);
		this.port = port;
	}
	
	public void start() throws Exception {
		if (isStarting || isStarted) {
			return;
		}
		isStarting = true;
		try {
			startServerSocket(port);
		} catch (Exception e) {
			logger.error(e.getMessage());
			logger.trace(e.getMessage(), e);
			isStarting = false;
			throw e;
		}
		isStarted = true;
		
		listen();
	}
	
	public void close() {
		try {
			serverChannel.close();
			logger.info("Server Socket closed on port: " + port);
		} catch (IOException e) {
			logger.error("Failed to close Server Socket on port: " + port);
			logger.trace(e.getMessage(), e);
		}
	}
	
	private ServerSocketChannel serverChannel = null;
	private Selector selector = null;
	
	private void listen() throws Exception {
		final RemotingTransporterServer _this = this;
		threadPool.execute(new Runnable() {
			public void run() {
				try {
					while (true) {
			            selector.select();
			            
			            Iterator<SelectionKey> ite = selector.selectedKeys().iterator();
			            
			            while (ite.hasNext()) {
			                final SelectionKey key = ite.next();
			                
			                ite.remove();
			                
			                if (key.isAcceptable()) {
			                    ServerSocketChannel server = (ServerSocketChannel)key.channel();
			                    SocketChannel channel = server.accept();
			                    channel.configureBlocking(false);
			                    
			                    channel.register(selector, SelectionKey.OP_READ);
			                    
			                    logger.info("New socket client is connected to this server.");
			                } else if (key.isReadable()) {
			                    final SocketChannel channel = (SocketChannel)key.channel();
			                    try {
			                    	msgHandler.handle(channel, new Callback() {
										public Object doCallback(Object... params) {
											String msg = (String) params[0];
											logger.debug("Receive message from client: " + msg);
											
											Action action = Action.parseJson(msg);
						            		
											if (action == null) {
												try {
													// If the action message is invalid
													// Close this client connection
													key.channel().close();
												} catch (IOException e) {
													logger.error(e.getMessage());
													logger.trace(e.getMessage(), e);
												}
											} else {
												ActionService actionService = ActionServiceFactory.getInstance().getService(action);
							            		actionService.execute(action, channel, _this);
											}
											return null;
										}
				                    });
			                    } catch (IOException e) {
			                    	logger.warn("Client lost, ignore this selection key.");
			                    	
			                    	/** cancel the selection key to remove the socket handling
			                         * from selector. This is to prevent netcat problem wherein
			                         * netcat immediately closes the sending side after sending the
			                         * commands and still keeps the receiving channel open.
			                         * The idea is to remove the selectionkey from the selector
			                         * so that the selector does not notice the closed read on the
			                         * socket channel and keep the socket alive to write the data to
			                         * and makes sure to close the socket after its done writing the data
			                         */
			                    	key.cancel();
			                    } catch (Exception e) {
			                    	logger.error(e.getMessage());
			    					logger.trace(e.getMessage(), e);
			                    }
			                }
			            }
			        }
				} catch (Exception e) {
					logger.error(e.getMessage());
					logger.trace(e.getMessage(), e);
				}
			}
		});
	}
	
	private void startServerSocket(final int port) throws Exception {
		try {
		    serverChannel = ServerSocketChannel.open();
		    serverChannel.configureBlocking(false);
		    serverChannel.socket().bind(new InetSocketAddress(port));
		    
		    selector = Selector.open();
		    
		    serverChannel.register(selector, SelectionKey.OP_ACCEPT);
		    
		} catch (IOException e) {
			logger.error("Server Socket start failed on port: " + port);
			logger.trace(e.getMessage(), e);
			throw e;
		}
		
        logger.info("Server Socket started on port: " + port);
	}

	public void updateLastAvailableTimestamp4ClientConnected(String name) {
		Node client = clientsConnectedToServer.get(name);
		if (client == null) {
			client = new Node(name, new URL(""));
			clientsConnectedToServer.put(name, client);
		}
		Long currTimestamp = System.currentTimeMillis();
		client.setLastAvailableTimestamp(currTimestamp);
	}
	
	public Map<String, Node> getClientsConnectedToServer() {
		return clientsConnectedToServer;
	}
	
	public void shutdown() {
		close();
	}
	
	private Map<String, SocketChannel> clientChannelMap = new ConcurrentHashMap<String, SocketChannel>();
	
	public void updateClientChannel(String clientUniqName, SocketChannel channel) {
		clientChannelMap.put(clientUniqName, channel);
	}
	
	public void sendMsgToClient(String clientUniqName, String msg) {
		SocketChannel channel = clientChannelMap.get(clientUniqName);
		if (channel == null)
			return;
		try {
			NioSocketUtil.sendMsg(channel, msg);
		} catch (IOException e) {
			logger.warn(e.getMessage(), e);
			clientChannelMap.remove(clientUniqName);
		}
	}
}
