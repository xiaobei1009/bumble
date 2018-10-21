package org.bumble.client.springcloud.interceptor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestInterceptor;

import feign.RequestInterceptor;

@Configuration
public class SpringConfiguration {
	
	@Bean
    public RequestInterceptor requestFeignInterceptor(){
        return new BumbleClientFeignRequestInterceptor();
    }
	
	@Bean
	public ClientHttpRequestInterceptor httpRequestInterceptor() {
		return new BumbleClientHttpRequestInterceptor();
	}
}
