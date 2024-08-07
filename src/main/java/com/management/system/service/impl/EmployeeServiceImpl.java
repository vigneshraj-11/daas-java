package com.management.system.service.impl;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.management.system.dto.EmployeeDto;
import com.management.system.dto.OrganizationDTO;
import com.management.system.entity.EmployeeEntity;
import com.management.system.entity.OrganizationEntity;
import com.management.system.repository.EmployeeRepository;
import com.management.system.service.EmployeeService;

@Service
public class EmployeeServiceImpl implements EmployeeService {

	private final EmployeeRepository employeeRepository;

	public EmployeeServiceImpl(EmployeeRepository employeeRepository) {
		this.employeeRepository = employeeRepository;
	}

	public boolean canAccessUser(Integer userId, String username) {
		EmployeeEntity employee = findEmployeeById(userId);
		return employee.getEmail().equals(username);
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

	@Transactional
	public List<EmployeeDto> getAllEmployees() {
		Iterable<EmployeeEntity> employees = employeeRepository.findAll();
		return ((Collection<EmployeeEntity>) employees).stream().map(this::convertToDTO).collect(Collectors.toList());
	}

	private EmployeeDto convertToDTO(EmployeeEntity employeeEntity) {
		EmployeeDto employeeDto = new EmployeeDto();
		employeeDto.setId(employeeEntity.getId());
		employeeDto.setUserNumber(employeeEntity.getEmployeeNumber());
		employeeDto.setFullName(employeeEntity.getFullName());
		employeeDto.setEmail(employeeEntity.getEmail());
		employeeDto.setDepartment(employeeEntity.getDepartment());
		employeeDto.setDestination(employeeEntity.getDestination());
		employeeDto.setActiveStatus(employeeEntity.getActiveStatus());
		employeeDto.setFirstName(employeeEntity.getFirstName());
		employeeDto.setLastName(employeeEntity.getLastName());
		employeeDto.setAddress(employeeEntity.getAddress());
		employeeDto.setGender(employeeEntity.getGender());
		employeeDto.setMobileNumber(employeeEntity.getMobileNumber());
		employeeDto.setProfilePicture(employeeEntity.getProfilePicture());
		employeeDto.setAuthority(employeeEntity.getRole().name());

		OrganizationDTO organizationDTO = new OrganizationDTO();
		OrganizationEntity organizationEntity = employeeEntity.getOrganization();
		if (organizationEntity != null) {
			organizationDTO.setId(organizationEntity.getId());
			organizationDTO.setOrganizationName(organizationEntity.getOrganizationName());
			employeeDto.setOrganization(organizationDTO);
		}

		return employeeDto;
	}

	@Override
	public List<EmployeeDto> getAllEmployeesExcludingCurrent(String currentUserEmail) {
		Iterable<EmployeeEntity> employees = employeeRepository
				.findByEmailNotAndActiveStatusAndNotRoleSuperadmin(currentUserEmail, "Active");
		return ((Collection<EmployeeEntity>) employees).stream().map(this::convertToDTO).collect(Collectors.toList());
	}

	@Override
	public String getUserRoleById(Integer userId) {
		EmployeeEntity user = employeeRepository.findById(userId)
				.orElseThrow(() -> new RuntimeException("User not found"));
		return user.getRole().name();
	}
}