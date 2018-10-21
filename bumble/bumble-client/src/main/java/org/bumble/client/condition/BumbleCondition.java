package org.bumble.client.condition;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BumbleCondition {
	
	public interface State {
		public final static String INIT = "init";
		public final static String SUCCESS = "success";
		public final static String FAIL = "fail";
		public final static String TIMEOUT = "timeout";
		public final static String TERMINATED = "terminated";
	}
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	private String id = null;
	private Lock lock;
    private Condition condition;
    private String state = null;
    private int awaitTimeout = 10000;
    
    /**
     * If the signal action is before await, then skipAwait flag will be set
     * and the following await will be skipped
     */
    private Boolean _skipAwait = false;
    
    BumbleCondition(String id) {
    	this.id = id;
    	this.setState(BumbleCondition.State.INIT);
        lock = new ReentrantLock();
        condition = lock.newCondition();
    }
    
    public String getId() {
    	return id;
    }
    
    public String getState() {
    	return state;
    }
    
    public void setState(String state) {
    	this.state = state;
    }
    
    private Boolean _isAwaiting = false;
    
    public void await() throws Exception {
    	lock.lock();
        final BumbleCondition _this = this;
    	_isAwaiting = true;
        
        if (_skipAwait) {
        	lock.unlock();
        	return;
        }
        
        // Set timer for checking timeout
        Timer timer = new Timer();
		timer = new Timer();
		
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				_this.setState(BumbleCondition.State.TIMEOUT);
				_this.trySignal();
			}
		}, awaitTimeout);
		
        condition.await();
        timer.cancel();
        
        try {
        	checkState();
        } catch (Exception e) {
        	throw e;
        } finally {
        	lock.unlock();
        }
    }
    
    /**
     * Try to signal after await
     */
    public void trySignal() {
    	lock.lock();
    	if (!_isAwaiting)
    		return;
    	
    	condition.signal();
    	_isAwaiting = false;
    	lock.unlock();
    }
    
    /**
     * Signal the condition
     * <p>
     * {@link BumbleCondition#_skipAwait} will be used when signal is before await
     */
    public void signal() {
    	lock.lock();
    	if (_isAwaiting) {
    		trySignal();
    	} else {
    		_skipAwait = true;
    	}
    	lock.unlock();
    }
    
	public Boolean isAwaiting() {
		return _isAwaiting;
	}
	
	private void checkState() throws Exception {
		String msg = null;
		if (state.equals(BumbleCondition.State.TIMEOUT)) {
			msg = "Condition is timed out, all the waiting thread will be notified with Exception.";
		} else if (state.equals(BumbleCondition.State.TERMINATED)) {
			msg = "Condition is terminated, all the waiting thread will be notified with Exception.";
		}
		if (msg != null) {
			logger.warn(msg);
			throw new Exception(msg);
		}
	}

	public int getAwaitTimeout() {
		return awaitTimeout;
	}

	public void setAwaitTimeout(int awaitTimeout) {
		this.awaitTimeout = awaitTimeout;
	}
}
