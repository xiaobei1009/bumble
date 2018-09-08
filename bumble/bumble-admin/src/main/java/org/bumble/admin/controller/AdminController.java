package org.bumble.admin.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import org.bumble.registry.Registry;
import org.bumble.registry.RegistryFactory;
import org.bumble.registry.data.RegistryData;

@RestController
@RequestMapping("/admin")
public class AdminController {

	Registry registry = RegistryFactory.getRegistry();
	
	@RequestMapping("/hello")
	public String hello(HttpServletRequest request, HttpServletResponse response, @RequestParam Map<String, String> map) {
		String ret = "hello msg monitor controller";
		
		return ret;
	}
	
	@RequestMapping("/get-registry-data")
	public String getRegistryData(HttpServletRequest request, HttpServletResponse response, @RequestParam Map<String, String> map) {
		RegistryData rd = registry.getData();
        String ret = rd.toJsonString();
		return ret;
	}
	
	@RequestMapping("/clear-registry")
	public String clearRegistry(HttpServletRequest request, HttpServletResponse response, @RequestParam Map<String, String> map) {
		registry.clear();
		String ret = "registry cleared";
		return ret;
	}
}
