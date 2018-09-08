package org.bumble.admin.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

@RestController
@RequestMapping("/bumble-admin/api/login")
public class LoginController {
	
	@RequestMapping("/hello")
	public String hello(HttpServletRequest request, HttpServletResponse response, @RequestParam Map<String, String> map) {
		String ret = "hello msg bumble-admin controller";
		return ret;
	}
	
	@RequestMapping("/login")
	public String login(HttpServletRequest request, HttpServletResponse response, @RequestParam Map<String, String> map) {
		String ret = "hello msg bumble-admin controller";
		JSONArray roles = new JSONArray();
		roles.add("admin");
		
		JSONObject json = new JSONObject();
		json.put("roles", roles);
		json.put("token", "admin");
		json.put("introduction", "我是超级管理员");
		json.put("avatar", "static/img/guitar-rabbit-80x80.gif");
		json.put("name", "Super Admin");
		
		ret = json.toJSONString();
		
		return ret;
	}
	
	@RequestMapping("/logout")
	public String logout(HttpServletRequest request, HttpServletResponse response, @RequestParam Map<String, String> map) {
		String ret = "success";
		return ret;
	}
}
