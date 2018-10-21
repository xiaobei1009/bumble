package org.bumble.admin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * Hello world!
 *
 */
@EnableAutoConfiguration
@SpringBootApplication
@ServletComponentScan
@EnableAspectJAutoProxy
public class BumbleAdminApp 
{
    public static void main( String[] args )
    {
        System.out.println( "Bumble Admin Hello!" );
        SpringApplication.run(BumbleAdminApp.class, args);
    }
}
