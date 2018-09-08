package org.bumble.demo.controller;

import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.bumble.demo.service.DemoService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/demo")
public class DemoController {

	@Resource(name="demoService")
	DemoService demoService;
	
	@RequestMapping("/hello")
	public String hello(HttpServletRequest request, HttpServletResponse response, @RequestParam Map<String, String> map) {
		
		String serviceRet = demoService.hello();
		
		String ret = 
			serviceRet + 
			"<br>" +
			"hello msg from demo1 controller";
		return ret;
	}
	
	@RequestMapping("/hello-with-err")
	public String helloWithErr(HttpServletRequest request, HttpServletResponse response, @RequestParam Map<String, String> map) {
		
		String serviceRet = demoService.helloWithErr();
		
		String ret = 
			serviceRet + 
			"<br>" +
			"hello msg from demo1 controller";
		return ret;
	}
}
