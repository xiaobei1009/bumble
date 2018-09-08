package org.bumble.demo.config;

import org.bumble.demo.interceptor.DemoRequestInterceptor;
import org.springframework.context.annotation.Bean;

import feign.RequestInterceptor;

public class MyConfiguration {
	
	@Bean
    public RequestInterceptor requestInterceptor(){
        return new DemoRequestInterceptor();
    }
}
