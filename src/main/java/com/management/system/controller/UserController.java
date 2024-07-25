package com.management.system.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("${endpoints}/user")
public class UserController {

	@Operation(summary = "Server test", description = "Returns a simple message indicating the server is running.")
	@Tag(name = "Server test")
	@GetMapping("/test")
	public String testAdmin() {
		return "Hello User";
	}
	
}
