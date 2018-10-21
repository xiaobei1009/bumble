package org.bumble.client.springcloud.interceptor;

import org.bumble.core.BumbleConst;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.bumble.client.threadlocal.TxnThreadLocal;

import feign.RequestInterceptor;
import feign.RequestTemplate;

public class BumbleClientFeignRequestInterceptor implements RequestInterceptor {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    public void apply(RequestTemplate requestTemplate) {
    	TxnThreadLocal current = TxnThreadLocal.getCurrent();
    	if (current != null) {
    		String txnGroupId = TxnThreadLocal.getCurrent().getTxnGroupId();
        	if (txnGroupId != null) {
        		logger.debug("txnGroupId:" + txnGroupId);
        		requestTemplate.header(BumbleConst.TXN_GROUP_ID, txnGroupId);
        	}
        	String starterManagerUniqName = TxnThreadLocal.getCurrent().getStarterManagerUniqName();
        	if (starterManagerUniqName != null) {
        		logger.debug("starterManagerUniqName:" + starterManagerUniqName);
        		requestTemplate.header(BumbleConst.STARTER_MANAGER_UNIQ_NAME, starterManagerUniqName);
        	}
    	}
    }

}
