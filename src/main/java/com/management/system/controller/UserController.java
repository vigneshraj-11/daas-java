package com.management.system.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.management.system.dto.EmployeeDto;
import com.management.system.dto.OrganizationDTO;
import com.management.system.entity.EmployeeEntity;
import com.management.system.entity.OrganizationEntity;
import com.management.system.enumcollection.Role;
import com.management.system.service.CountService;
import com.management.system.service.EmployeeService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("${endpoints}/user")
public class UserController {

	private final EmployeeService employeeService;

	@Autowired
	private CountService countService;

	@Autowired
	public UserController(EmployeeService employeeService) {
		this.employeeService = employeeService;
	}

	@Operation(summary = "User Info", description = "Get user information via user id.")
	@Tag(name = "User Information")
	@GetMapping("/{userId}")
	public EmployeeDto getUserInfo(@PathVariable Integer userId) {
		EmployeeEntity employee = employeeService.findEmployeeById(userId);
		return mapToDTO(employee);
	}

	@Operation(summary = "User Info", description = "Returns total number of documents uploaded by User.")
	@Tag(name = "User Information")
	@GetMapping("/documentcounts/{userId}")
	public long getTotalDocumentCount(@PathVariable Integer userId) {
		return countService.getTotalDocumentCountByUserId(userId);
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
}
