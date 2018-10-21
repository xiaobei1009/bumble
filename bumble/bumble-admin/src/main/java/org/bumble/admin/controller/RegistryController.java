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
@RequestMapping("/bumble-admin/api/registry")
public class RegistryController {
	
	@RequestMapping("/hello")
	public String hello(HttpServletRequest request, HttpServletResponse response, @RequestParam Map<String, String> map) {
		String ret = "hello msg bumble-admin controller";
		return ret;
	}
	
	@RequestMapping("/all")
	public String getAllRegistry(HttpServletRequest request, HttpServletResponse response, @RequestParam Map<String, String> map) {
		Registry registry = RegistryFactory.getRegistry();
		RegistryData registryData = registry.getData();
		String ret = registryData.toJsonString();
		return ret;
	}
	
}
