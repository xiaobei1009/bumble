package org.bumble.demo.interceptor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import feign.RequestInterceptor;
import feign.RequestTemplate;

public class DemoRequestInterceptor implements RequestInterceptor {

	Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Override
	public void apply(RequestTemplate requestTemplate) {
		logger.info("request intercept - bumble test 111");
		requestTemplate.header("bumble-test", "111");
	}

}
