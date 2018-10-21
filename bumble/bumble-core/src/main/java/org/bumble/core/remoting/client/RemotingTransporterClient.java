package org.bumble.core.remoting.client;

import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;

import org.bumble.base.BumbleNameBuilder;
import org.bumble.base.Callback;
import org.bumble.base.util.NioSocketUtil;
import org.bumble.core.BumbleConst;
import org.bumble.core.BumbleIdentifier;
import org.bumble.core.action.Action;
import org.bumble.core.action.ActionConst;
import org.bumble.core.action.ActionService;
import org.bumble.core.action.ActionServiceFactory;
import org.bumble.core.remoting.RemotingTransporter;

public class RemotingTransporterClient 
	extends RemotingTransporter 
	implements IRemotingTransporterClient {
	
	private String ip = null;
	private int port = 0;
	
	public RemotingTransporterClient(ExecutorService threadPool, String name, String ip, int port) {
		super(threadPool, name);
		this.ip = ip;
		this.port = port;
		initHeartAction();
	}
	
	private String heartMsg = null;
	
	private void initHeartAction() {
		try {
			Action heartAction = new Action(ActionConst.Type.HEART_REQ);
			String uniqName = BumbleNameBuilder.getInstance().getUniqName();
			heartAction.setParamEntry(ActionConst.Param.REQ_FROM_UNIQ_NAME, uniqName);
	    	heartMsg = heartAction.toJsonString();
		} catch (Exception e) {
			logger.error(e.getMessage());
			logger.trace(e.getMessage(), e);
		}
	}
	
	private Boolean _isConnecting = false;
	private Boolean _isConnected = false;
	private Boolean _isFailedToConnect = false;
	private Boolean _isClosed = false;
	private Boolean _isConnectionLost = false;
	
	public Boolean isClosed() {
		return _isClosed;
	}
	
	public Boolean isConnecting() {
		return _isConnecting;
	}
	
	private volatile Long lastAvailableTimestamp = null;
	private Queue<String> msgQueue = new ConcurrentLinkedQueue<String>();
	
	public Boolean isFailedToConnect() {
		return this._isFailedToConnect;
	}

	public Boolean isConnected() {
		synchronized(connectStateLock) {
			return this._isConnected;
		}
	}
	
	public void setConnected(Boolean isConnected) {
		synchronized(connectStateLock) {
			this._isConnected = isConnected;
		}
	}
	
	public Long getLastAvailableTimestamp() {
		return this.lastAvailableTimestamp;
	}

	public void setLastAvailableTimestamp(Long lastAvailableTimestamp) {
		this.lastAvailableTimestamp = lastAvailableTimestamp;
	}

	public void close() {
		if (this._isClosed)
			return;
		logger.warn("Close connection to server [" + this.name + "]");

		this._isClosed = true;
		this._isConnecting = false;
		this.setConnected(false);
		
		// Notify the message send thread to terminate
		synchronized (msgSendLock) {
			if (isMsgSendThreadAwait) {
				isMsgSendThreadAwait = false;
				msgSendLock.notify();
			}
		}
	}
	
	public String getName() {
		return this.name;
	}
	
	private Selector selector = null;
	
	public void failedToConnect() {
		logger.debug("Failed to connect");
		this._isFailedToConnect = true;
		this.setConnected(false);
	}
	
	public void connectionErrorOccorred() {
		if (this.isConnected()) {
			this.lostConnection();
		} else {
			_isFailedToConnect = true;
			this.failedToConnect();
		}
	}
	
	private Object connectStateLock = new Object();
	private Object msgSendLock = new Object();
	private Boolean isMsgSendThreadAwait = false;
	
	private void listen() {
		final RemotingTransporterClient _this = this;
		threadPool.execute(new Runnable() {
			public void run() {
				try {
					while (!_this.isClosed()) {
			            selector.select();
			            Iterator<SelectionKey> ite = selector.selectedKeys().iterator();
			            while (ite.hasNext()) {
			                SelectionKey key = ite.next();
			                
			                ite.remove();
			                if (key.isConnectable()) {
			                    final SocketChannel channel = (SocketChannel) key.channel();
			                    
			                    if (channel.isConnectionPending()) {
			                    	try {
			                    		channel.finishConnect();
			                    	} catch (Exception e) {
			                    		logger.error(e.getMessage());
			                    		logger.trace(e.getMessage(), e);
			                    		_this.failedToConnect();
			                    		break;
			                    	}
			                    }
			                    
			                    channel.configureBlocking(false);

			                    channel.register(selector, SelectionKey.OP_READ);
			                    _this._isConnecting = false;
			                    _this._isConnectionLost = false;
			                    _this.setConnected(true);
			                    
			                    threadPool.execute(new Runnable() {
									public void run() {
										while (_this.isConnected()) {
									    	try {
									    		String msg = null;
								    			while ((msg = msgQueue.poll()) != null) {
										    		NioSocketUtil.sendMsg(channel, msg);
										    	}
										    	
								    			synchronized (msgSendLock) {
								    				if (msgQueue.isEmpty()) {
								    					isMsgSendThreadAwait = true;
										    			msgSendLock.wait();
								    				}
								    			}
											} catch (Exception e) {
												logger.error(e.getMessage());
		                            			logger.trace(e.getMessage(), e);
		                            			_this.lostConnection();
											}
										}
									}
			                    });
			                    
			                    if (enableHeart) {
			                    	threadPool.execute(new Runnable() {
			    						public void run() {
			    							while (_this.isConnected()) {
			    								try {
			    									logger.debug("SendHeartRequest to [" + _this.name + "]");
			                                    	
			    									if (_this.isConnected()) {
			    										NioSocketUtil.sendMsg(channel, heartMsg);
			    									}
			    									
			                            			// Heat Beat Interval
			    									int interval = RemotingTransporterClientFactory.getInstance().getHeartBeatInterval();
			                            			Thread.sleep(interval);
			                            		} catch (Exception e) {
			                            			logger.error(e.getMessage());
			                            			logger.trace(e.getMessage(), e);
			                            			_this.lostConnection();
			                            		}
			    							}
			    						}
			                        });
			                    }
			                    
			                    // Send connect success message to the server if it is a client
			                    if (BumbleIdentifier.getInstance().isClient()) {
			                    	Action connSuccessAction = new Action(ActionConst.Type.CONNECT_SUCCESS);
				                    String clientUniqName = BumbleNameBuilder.getInstance().getUniqName();
				                    connSuccessAction.setParamEntry(BumbleConst.CLIENT_UNIQ_NAME, clientUniqName);
				                    
				                    _this.sendMsg(connSuccessAction.toJsonString());
			                    }
			                    
			                    logger.info("Connect to [" + _this.name + "] success.");
			                } else if(key.isReadable()) {
			                	if (!_this.isConnected())
			                		break;
			                	
			                    final SocketChannel channel = (SocketChannel)key.channel();
			                    
			                    try {
				                    msgHandler.handle(channel, new Callback() {
										public Object doCallback(Object... params) {
											String msg = (String) params[0];
											logger.debug("Receive message from server: " + msg);
											
											Action action = Action.parseJson(msg);
						            		
						            		ActionService actionService = ActionServiceFactory.getInstance().getService(action);
						            		actionService.execute(action, channel, _this);
											return null;
										}
				                    });
				                 } catch (Exception e) {
				                	 _this.connectionErrorOccorred();
				                 }
			                }
			            }
			            if (_this.isConnectionLost()) {
							_this.start4ConnectionLost();
							break;
						} else if (_this.isFailedToConnect()) {
							_this.tryConnectQueue.add(new Object());
							break;
						}
					}
					logger.debug("Socket Listener exit");
				} catch (Exception e) {
					logger.error(e.getMessage());
					logger.trace(e.getMessage(), e);
				}
			}
		});
	}
	
	private Boolean enableHeart = true;
	private Queue<Object> tryConnectQueue = new ConcurrentLinkedQueue<Object>();
	
	public void enableHeart(Boolean enableHeart) {
		this.enableHeart = enableHeart;
	}
	
	public void restart() throws Exception {
		logger.debug("Restart");
		this.close();
		this.start();
	}
	
	public void start4ConnectionLost() throws Exception {
		logger.debug("start4ConnectionLost");
		
		this.setConnected(false);
		
		this._isConnecting = false;
		this._isFailedToConnect = false;
		this._isClosed = false;
		this._isConnectionLost = false;
		
		start();
	}
	
	public void start() throws Exception {
		if (_isConnecting || this.isConnected()) {
			return;
		}
		_isConnecting = true;
		
		int tryTimes = 0;
		tryConnectQueue.add(new Object());
		while (true) {
			if (this.isConnected()) {
				logger.debug("Connected, break out from the try connect loop");
				break;
			}
			if (tryTimes >= 3) {
				RemotingTransporterClientFactory.getInstance().closeClient(this);
				// The client is set to be closed
				break;
			}
			
			if (tryConnectQueue.poll() != null) {
				tryTimes++;
				logger.info("Try to connect [" + name + "] " + tryTimes + " time(s)");
				
				tryConnect();
				
			}
			
			// Retry connect interval
			int interval = RemotingTransporterClientFactory.getInstance().getSocketConnectRetryInterval();
			Thread.sleep(interval);
		}
        
	}
	
	private void tryConnect() throws Exception {
		
		SocketChannel channel = SocketChannel.open();
        
        channel.configureBlocking(false);
        
        selector = Selector.open();
        
        channel.connect(new InetSocketAddress(ip, port));
        
        channel.register(selector, SelectionKey.OP_CONNECT);
        
		listen();
	}
	
	public void sendMsg(String msg) {
		synchronized (msgSendLock) {
			msgQueue.add(msg);
			if (isMsgSendThreadAwait) {
				isMsgSendThreadAwait = false;
				msgSendLock.notify();
			}
		}
	}

	public Boolean isConnectionLost() {
		return this._isConnectionLost;
	}

	public void lostConnection() {
		logger.debug("Connection lost.");
		this._isConnectionLost = true;
		this.setConnected(false);
	}
}
