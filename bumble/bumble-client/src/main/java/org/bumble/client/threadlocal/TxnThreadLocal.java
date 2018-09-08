package org.bumble.client.threadlocal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TxnThreadLocal {
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	public TxnThreadLocal(String txnId, String txnGroupId) {
    	this.txnId = txnId;
    	this.txnGroupId = txnGroupId;
    	logger.debug("New Transaction Thread Local - txnId: " + txnId + ", txnGroupId: " + txnGroupId);
    }
	
    private String txnId = null;
    private String txnGroupId = null;
    private String starterManagerUniqName = null;
    
    private final static ThreadLocal<TxnThreadLocal> currentLocal = new InheritableThreadLocal<TxnThreadLocal>();
    
    public static TxnThreadLocal getCurrent() {
    	return currentLocal.get();
    }
    
    public static void setCurrent(TxnThreadLocal current) {
    	currentLocal.set(current);
    }
    
    public String getTxnId() {
		return txnId;
	}

	public void setTxnId(String txnId) {
		this.txnId = txnId;
	}

	public String getTxnGroupId() {
		return txnGroupId;
	}

	public void setTxnGroupId(String txnGroupId) {
		this.txnGroupId = txnGroupId;
	}

	public String getStarterManagerUniqName() {
		return starterManagerUniqName;
	}

	public void setStarterManagerUniqName(String starterManagerUniqName) {
		this.starterManagerUniqName = starterManagerUniqName;
	}
    
}
