package org.bumble.demo;

import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * Hello world!
 *
 */
@EnableAutoConfiguration
@SpringBootApplication
@MapperScan({"org.bumble.demo.dao"})
@ServletComponentScan
@EnableAspectJAutoProxy
@EnableEurekaClient
@EnableFeignClients
public class BumbleSpringcloudDemo1App
{
	private static Logger logger = LoggerFactory.getLogger(BumbleSpringcloudDemo1App.class);
	
    public static void main( String[] args )
    {
        logger.debug("spring cloud demo1 start!");
        
        SpringApplication.run(BumbleSpringcloudDemo1App.class, args);
    }
}
