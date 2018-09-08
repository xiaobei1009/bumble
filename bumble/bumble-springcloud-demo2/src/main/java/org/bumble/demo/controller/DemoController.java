package org.bumble.demo.controller;

import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.bumble.demo.service.DemoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/demo")
public class DemoController {
	
	Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Resource(name="demoService")
	DemoService demoService;
	
	@RequestMapping("/hello")
	public String hello(HttpServletRequest request, HttpServletResponse response, @RequestParam Map<String, String> map) {
		logger.info("hello msg from demo2 controller");
		
		demoService.newUser();
		
		return "hello msg from demo2 controller";
	}
	
	@RequestMapping("/hello-with-err")
	public String helloWithErr(HttpServletRequest request, HttpServletResponse response, @RequestParam Map<String, String> map) {
		logger.info("helloWithErr msg from demo2 controller");
		
		demoService.newUserWithErr();
		
		return "helloWithErr msg from demo2 controller";
	}
}
