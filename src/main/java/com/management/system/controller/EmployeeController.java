package com.management.system.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.management.system.dto.EmployeeDto;
import com.management.system.dto.OrganizationDTO;
import com.management.system.entity.EmployeeEntity;
import com.management.system.entity.OrganizationEntity;
import com.management.system.enumcollection.Role;
import com.management.system.repository.EmployeeRepository;
import com.management.system.service.EmployeeService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("${endpoints}/user")
public class EmployeeController {

	@Autowired
	private EmployeeService employeeService;

	@Autowired
	private EmployeeRepository employeeRepository;

	@Operation(summary = "User Info", description = "Get user information via user mail.")
	@Tag(name = "User Information")
	@GetMapping("/bymail/{mail}")
	public EmployeeDto getUserByEmail(@PathVariable String mail) {
		EmployeeEntity employee = employeeService.getEmployeeByEmail(mail);
		return mapToDTO(employee);
	}

	private EmployeeDto mapToDTO(EmployeeEntity employee) {
		EmployeeDto dto = new EmployeeDto();
		dto.setId(employee.getId());
		dto.setUserNumber(employee.getEmployeeNumber());
		dto.setFullName(employee.getFullName());
		dto.setFirstName(employee.getFirstName());
		dto.setLastName(employee.getLastName());
		dto.setProfilePicture(employee.getProfilePicture());
		dto.setAuthority(employee.getRole() == Role.USER ? "USER"
				: employee.getRole() == Role.ADMIN ? "ADMIN"
						: employee.getRole() == Role.SUPERADMIN ? "SUPERADMIN" : "UNKNOWN");
		dto.setEmail(employee.getEmail());
		dto.setDepartment(employee.getDepartment());
		dto.setDestination(employee.getDestination());
		dto.setMobileNumber(employee.getMobileNumber());
		dto.setAddress(employee.getAddress());
		dto.setActiveStatus(employee.getActiveStatus());
		if (employee.getOrganization() != null) {
			dto.setOrganization(mapOrganizationToDTO(employee.getOrganization()));
		}
		return dto;
	}

	private OrganizationDTO mapOrganizationToDTO(OrganizationEntity organization) {
		OrganizationDTO dto = new OrganizationDTO();
		dto.setId(organization.getId());
		dto.setOrganizationName(organization.getOrganizationName());
		return dto;
	}

	@Operation(summary = "Get All Users", description = "This API is used to get a list of all Users.")
	@Tag(name = "User Information")
	@GetMapping("/getAllUser")
	public ResponseEntity<List<EmployeeDto>> getAllUsers() {
		List<EmployeeDto> employees = employeeService.getAllEmployees();
		return ResponseEntity.ok(employees);
	}

	@Operation(summary = "Get All Users Excluding Current user", description = "This API is used to get a list of all Users excluding the current user specified by their email address.")
	@Tag(name = "User Information")
	@GetMapping("/leaveCurrentUser")
	public ResponseEntity<List<EmployeeDto>> getAllUsersExcludingCurrent(@RequestParam String currentUserEmail) {
		List<EmployeeDto> employees = employeeService.getAllEmployeesExcludingCurrent(currentUserEmail);
		return ResponseEntity.ok(employees);
	}

	@Operation(summary = "Delete User", description = "This API is used to set the active status of an user to 'Inactive' using their User ID.")
	@Tag(name = "User Information")
	@PostMapping("/deleteUser")
	public EmployeeEntity deleteEmployee(@RequestParam Integer id) {
		EmployeeEntity employee = employeeRepository.findById(id).orElseThrow();
		employee.setActiveStatus("Inactive");
		return employeeRepository.save(employee);
	}

}
