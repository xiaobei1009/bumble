package org.bumble.eureka.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * Hello world!
 *
 */
@SpringBootApplication
@EnableEurekaServer
public class BumbleEurekaServerApp 
{
    public static void main( String[] args )
    {
        System.out.println( "Bumble Eureka Server Hello!" );
        SpringApplication.run(BumbleEurekaServerApp.class, args);
    }
}
