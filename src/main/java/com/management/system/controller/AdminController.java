package com.management.system.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.management.system.dto.BatchSummaryDTO;
import com.management.system.dto.ResponseDTO;
import com.management.system.entity.EmployeeEntity;
import com.management.system.service.BatchService;
import com.management.system.service.CountService;
import com.management.system.service.EmployeeService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("${endpoints}/dashboard")
public class AdminController {

	@Autowired
	private CountService countService;

	@Autowired
	private BatchService batchService;

	@Autowired
	private EmployeeService employeeService;

	public AdminController(CountService countService) {
		this.countService = countService;
	}

	@Operation(summary = "User count", description = "Returns a user count by User ID.")
	@Tag(name = "Dashbaord")
	@GetMapping("/totalUsers/{userId}")
	public ResponseEntity<?> getTotalEmployeeCount(@PathVariable Integer userId) {
		EmployeeEntity employee = employeeService.findEmployeeById(userId);
		if (employee.getRole().toString().equals("SUPERADMIN")) {
			long count = countService.getTotalEmployeeCount();
			return ResponseEntity.ok(count);
		} else if (employee.getRole().toString().equals("ADMIN")) {
			long count = countService.getTotalEmployeeCount(userId);
			return ResponseEntity.ok(count);
		} else {
			ResponseDTO responseDTO = new ResponseDTO("Unauthorized", "You're not authorized to access this", null);
			return ResponseEntity.status(HttpStatus.REQUEST_TIMEOUT).body(responseDTO);
		}
	}

	@Operation(summary = "Dashboard Count Details", description = "Return counts of document and pages by UserID.")
	@Tag(name = "Dashbaord")
	@GetMapping("/dashbaordDetails/{userId}")
	public ResponseEntity<?> getTotalDocumentCount(@PathVariable Integer userId) {
		EmployeeEntity employee = employeeService.findEmployeeById(userId);
		if (employee.getRole().toString().equals("SUPERADMIN")) {
			BatchSummaryDTO batchSummary = batchService.getBatchSummary();
			return ResponseEntity.ok(batchSummary);
		} else if (employee.getRole().toString().equals("ADMIN")) {
			BatchSummaryDTO batchSummary = batchService.getBatchSummaryByOrgId(userId);
			return ResponseEntity.ok(batchSummary);
		} else {
			BatchSummaryDTO batchSummary = batchService.getBatchSummaryByUserId(userId);
			return ResponseEntity.ok(batchSummary);
		}
	}

	@Operation(summary = "Page Details", description = "Returns a date wise page details.")
	@Tag(name = "Dashbaord")
	@GetMapping("/getPageDetails/{userId}")
	public ResponseEntity<Map<String, Long>> getPageDetails(@PathVariable Integer userId) {
		EmployeeEntity employee = employeeService.findEmployeeById(userId);
		if (employee.getRole().toString().equals("SUPERADMIN")) {
			Map<String, Long> weeklySummary = batchService.getCurrentWeekPageSummary();
			return ResponseEntity.ok(weeklySummary);
		} else if (employee.getRole().toString().equals("ADMIN")) {
			Map<String, Long> weeklySummary = batchService.getCurrentWeekPageSummaryByOrgId(userId);
			return ResponseEntity.ok(weeklySummary);
		} else {
			Map<String, Long> weeklySummary = batchService.getCurrentWeekPageSummaryByUserId(userId);
			return ResponseEntity.ok(weeklySummary);
		}
	}

}
