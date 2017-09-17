package com.studiomediatech.security.rabbitmqauth;

import org.springframework.web.bind.annotation.GetMapping;

@org.springframework.stereotype.Controller
public class Controller {

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
