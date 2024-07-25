package com.management.system.service;

import java.util.List;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.management.system.entity.EmployeeEntity;

public interface EmployeeService {
	
	UserDetailsService employeeDetailService();
	
	public EmployeeEntity findEmployeeById(Integer id);
	 
	public List<EmployeeEntity> searchEmployee(String query);

	EmployeeEntity getEmployeeByEmail(String email);

	List<EmployeeEntity> getAllEmployees();

	List<EmployeeEntity> getAllEmployeesExcludingCurrent(String currentUserEmail);
	
}
