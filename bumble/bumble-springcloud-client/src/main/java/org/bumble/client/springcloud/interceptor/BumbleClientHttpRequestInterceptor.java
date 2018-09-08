package org.bumble.client.springcloud.interceptor;

import org.bumble.core.BumbleConst;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import org.bumble.client.threadlocal.TxnThreadLocal;

import java.io.IOException;

public class BumbleClientHttpRequestInterceptor implements ClientHttpRequestInterceptor {
	
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
    	String txnGroupId = TxnThreadLocal.getCurrent().getTxnGroupId();
    	if (txnGroupId != null) {
    		logger.debug("txnGroupId:" + txnGroupId);
    		request.getHeaders().add(BumbleConst.TXN_GROUP_ID, txnGroupId);
    	}
    	String starterManagerUniqName = TxnThreadLocal.getCurrent().getStarterManagerUniqName();
    	if (starterManagerUniqName != null) {
    		logger.debug("starterManagerUniqName:" + starterManagerUniqName);
    		request.getHeaders().add(BumbleConst.STARTER_MANAGER_UNIQ_NAME, starterManagerUniqName);
    	}
        return execution.execute(request, body);
    }
}
