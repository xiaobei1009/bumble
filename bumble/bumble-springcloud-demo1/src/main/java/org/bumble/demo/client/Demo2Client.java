package org.bumble.demo.client;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;

//@FeignClient(value = "demo2", configuration = MyConfiguration.class)
@FeignClient(value = "demo2")
public interface Demo2Client {

    @RequestMapping("/demo/hello")
    String hello();
    
    @RequestMapping("/demo/hello-with-err")
    String helloWithErr();
}
