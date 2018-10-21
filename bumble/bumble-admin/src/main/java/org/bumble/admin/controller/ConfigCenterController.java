package org.bumble.admin.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import org.bumble.config.Configurator;
import org.bumble.config.ConfiguratorFactory;

@RestController
@RequestMapping("/bumble-admin/api/config")
public class ConfigCenterController {

	@RequestMapping("/hello")
	public String hello(HttpServletRequest request, HttpServletResponse response, @RequestParam Map<String, String> map) {
		String ret = "hello msg bumble-admin controller";
		return ret;
	}
	
	@RequestMapping("/all")
	public String getAllConfig(HttpServletRequest request, HttpServletResponse response, @RequestParam Map<String, String> map) {
		Configurator configurator = ConfiguratorFactory.getConfigurator();
		String ret = configurator.getAllConfig();
		return ret;
	}
	
}
