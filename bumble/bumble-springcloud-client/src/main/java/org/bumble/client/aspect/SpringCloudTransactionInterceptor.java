package org.bumble.client.aspect;

import javax.servlet.http.HttpServletRequest;

import org.bumble.core.BumbleConst;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

public class SpringCloudTransactionInterceptor extends AbstractTransactionInterceptor {

	@Override
	protected String getTxnGroupId() {
		RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
        HttpServletRequest request = requestAttributes == null ? null : ((ServletRequestAttributes) requestAttributes).getRequest();
        String txnGroupId = request == null ? null : request.getHeader(BumbleConst.TXN_GROUP_ID);
		return txnGroupId;
	}

	@Override
	protected String getStarterManagerUniqName() {
		RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
        HttpServletRequest request = requestAttributes == null ? null : ((ServletRequestAttributes) requestAttributes).getRequest();
        String starterManagerUniqName = request == null ? null : request.getHeader(BumbleConst.STARTER_MANAGER_UNIQ_NAME);
		return starterManagerUniqName;
	}
	
}