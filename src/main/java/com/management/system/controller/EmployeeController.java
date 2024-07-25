package com.management.system.controller;

import com.management.system.entity.EmployeeEntity;
import com.management.system.repository.EmployeeRepository;
import com.management.system.service.EmployeeService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${endpoints}/employees")
public class EmployeeController {

	@Autowired
	private EmployeeService employeeService;

	@Autowired
	private EmployeeRepository employeeRepository;

	@Operation(summary = "Get Employee by Email", description = "This API is used to get an employee's details using their email address.")
	@Tag(name = "Employee")
	@GetMapping("/user")
	public ResponseEntity<EmployeeEntity> getUserByEmail(@RequestParam String email) {
		EmployeeEntity employee = employeeService.getEmployeeByEmail(email);
		if (employee != null) {
			return ResponseEntity.ok(employee);
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	@Operation(summary = "Get All Employees", description = "This API is used to get a list of all employees.")
	@Tag(name = "Employee")
	@GetMapping("/all")
	public ResponseEntity<List<EmployeeEntity>> getAllUsers() {
		List<EmployeeEntity> employees = employeeService.getAllEmployees();
		return ResponseEntity.ok(employees);
	}

	@Operation(summary = "Get All Employees Excluding Current", description = "This API is used to get a list of all employees excluding the current user specified by their email address.")
	@Tag(name = "Employee")
	@GetMapping("/all-excluding-current")
	public ResponseEntity<List<EmployeeEntity>> getAllUsersExcludingCurrent(@RequestParam String currentUserEmail) {
		List<EmployeeEntity> employees = employeeService.getAllEmployeesExcludingCurrent(currentUserEmail);
		return ResponseEntity.ok(employees);
	}

	@Operation(summary = "Delete Employee", description = "This API is used to set the active status of an employee to 'Inactive' using their employee ID.")
	@Tag(name = "Employee")
	@PostMapping("/delete")
	public EmployeeEntity deleteEmployee(@RequestParam Integer id) {
		EmployeeEntity employee = employeeRepository.findById(id).orElseThrow();
		employee.setActiveStatus("Inactive");
		return employeeRepository.save(employee);
	}
	
}
