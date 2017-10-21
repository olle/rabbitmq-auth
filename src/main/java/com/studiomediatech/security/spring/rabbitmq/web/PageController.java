package com.studiomediatech.security.spring.rabbitmq.web;

import org.springframework.web.bind.annotation.GetMapping;

@org.springframework.stereotype.Controller
public class PageController {

	@GetMapping({"/", "index"})
	public String index() {
		return "index";
	}
	
	@GetMapping("/user")
	public String user() {		
		return "user";
	}
	
	@GetMapping("/admin")
	public String admin() {
		return "admin";
	}
}
