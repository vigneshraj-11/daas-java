package com.management.system.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.management.system.service.CountService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("${endpoints}/admin")
public class AdminController {

	@Autowired
	private CountService countService;

	public AdminController(CountService countService) {
		this.countService = countService;
	}

	@Operation(summary = "Server test", description = "Returns a simple message indicating the server is running.")
	@Tag(name = "Admin")
	@GetMapping("/total-employees")
	public long getTotalEmployeeCount() {
		return countService.getTotalEmployeeCount();
	}

	@Operation(summary = "Server test", description = "Returns a simple message indicating the server is running.")
	@Tag(name = "Admin")
	@GetMapping("/total-documents")
	public long getTotalDocumentCount() {
		return countService.getTotalDocumentCount();
	}

	@Operation(summary = "Server test", description = "Returns a simple message indicating the server is running.")
	@Tag(name = "Admin")
	@GetMapping("/documents-by-type")
	public long getCountByType(@RequestParam String type) {
		return countService.getCountByType(type);
	}

	@Operation(summary = "Server test", description = "Returns a simple message indicating the server is running.")
	@Tag(name = "Server test")
	@GetMapping("/test")
	public String testAdmin() {
		return "Hello Admin";
	}

}
