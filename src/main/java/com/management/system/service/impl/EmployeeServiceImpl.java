package com.management.system.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.management.system.entity.EmployeeEntity;
import com.management.system.repository.EmployeeRepository;
import com.management.system.service.EmployeeService;

@Service
public class EmployeeServiceImpl implements EmployeeService {

	private final EmployeeRepository employeeRepository;

	public EmployeeServiceImpl(EmployeeRepository employeeRepository) {
		this.employeeRepository = employeeRepository;
	}

	@Override
	public UserDetailsService employeeDetailService() {
		return new UserDetailsService() {
			@Override
			public UserDetails loadUserByUsername(String username) {
				return employeeRepository.findByEmail(username)
						.orElseThrow(() -> new UsernameNotFoundException("User Not Found"));
			}
		};
	}

	@Override
	public EmployeeEntity findEmployeeById(Integer id) {
		Optional<EmployeeEntity> employee = employeeRepository.findById(id);
		if (employee.isPresent()) {
			return employee.get();
		}
		throw new UsernameNotFoundException("User Not Found");
	}

	@Override
	public List<EmployeeEntity> searchEmployee(String query) {
		List<EmployeeEntity> employees = employeeRepository.searchEmployee(query);
		return employees;
	}

	@Override
	public EmployeeEntity getEmployeeByEmail(String email) {
		Optional<EmployeeEntity> employee = employeeRepository.findByEmail(email);
		return employee.orElse(null);
	}

	@Override
	public List<EmployeeEntity> getAllEmployees() {
		Iterable<EmployeeEntity> iterable = employeeRepository.findAll();
		return StreamSupport.stream(iterable.spliterator(), false).collect(Collectors.toList());
	}

	@Override
	public List<EmployeeEntity> getAllEmployeesExcludingCurrent(String currentUserEmail) {
		return employeeRepository.findByEmailNotAndActiveStatusAndNotRoleSuperadmin(currentUserEmail, "Active");
	}

}