package com.management.system.service.impl;

import java.util.HashMap;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.management.system.dto.CreateEmployee;
import com.management.system.dto.EmployeeDto;
import com.management.system.dto.JwtAuthenticationResponse;
import com.management.system.dto.Login;
import com.management.system.dto.OrganizationDTO;
import com.management.system.dto.RefreshTokenRequest;
import com.management.system.entity.EmployeeEntity;
import com.management.system.entity.OrganizationEntity;
import com.management.system.enumcollection.Role;
import com.management.system.exception.EntityExistsException;
import com.management.system.repository.EmployeeRepository;
import com.management.system.service.AuthenticationService;
import com.management.system.service.EmailTemplateService;
import com.management.system.service.JWTService;
import com.management.system.utils.PasswordGenerator;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {

	@Autowired
	private EmployeeRepository employeeRepository;
	@Autowired
	private PasswordEncoder passwordEncoder;
	@Autowired
	private AuthenticationManager authenticationManager;
	@Autowired
	private JWTService jwtService;
	@Autowired
	private EmployeeNumberGeneratorService employeeNumberGeneratorService;
	@Value("${mail.sender.name}")
	private String senderName;
	@Autowired
	private EmailTemplateService emailTemplateService;

	@Transactional
	public EmployeeDto createEmployee(CreateEmployee createEmployee) {
		if (employeeRepository.findByEmail(createEmployee.getEmail()).isPresent()) {
			throw new EntityExistsException("Email already exists.");
		}

		EmployeeEntity employee = new EmployeeEntity();
		employee.setFirstName(createEmployee.getFirstName());
		employee.setLastName(createEmployee.getLastName());
		employee.setFullName(createEmployee.getFirstName() + " " + createEmployee.getLastName());
		employee.setCreatedBy(createEmployee.getCreatedBy());
		employee.setRole(createEmployee.getRole().equalsIgnoreCase("user") ? Role.USER : Role.ADMIN);
		employee.setEmail(createEmployee.getEmail());

		if (createEmployee.getCreatedBy() != 0) {
			Optional<EmployeeEntity> creator = employeeRepository.findById(createEmployee.getCreatedBy());
			creator.ifPresent(employeeEntity -> employee.setOrganization(employeeEntity.getOrganization()));
		}

		String employeeNumber = employeeNumberGeneratorService.generateEmployeeNumber();
		employee.setEmployeeNumber(employeeNumber);
		String rawPassword = PasswordGenerator.generateRandomPassword();
		employee.setPassword(passwordEncoder.encode(rawPassword));
		EmployeeEntity savedEmployee = employeeRepository.save(employee);

		emailTemplateService.sendWelcomeEmail(savedEmployee, employeeNumber, rawPassword);

		return convertToDto(savedEmployee);
	}

	public JwtAuthenticationResponse login(Login login) throws IllegalAccessException {
		authenticationManager
				.authenticate(new UsernamePasswordAuthenticationToken(login.getEmail(), login.getPassword()));
		var employee = employeeRepository.findByEmail(login.getEmail())
				.orElseThrow(() -> new IllegalAccessException("Invalid Email or Password"));
		EmployeeEntity employeeDetails = employeeRepository.findByEmail(login.getEmail()).orElseThrow();
		var jwt = jwtService.generateToken(employee);
		var refreshToken = jwtService.generateRefreshToken(new HashMap<>(), employee);
		JwtAuthenticationResponse jwtAuthenticationResposne = new JwtAuthenticationResponse();
		jwtAuthenticationResposne.setToken(jwt);
		jwtAuthenticationResposne.setRefreshToken(refreshToken);
		jwtAuthenticationResposne.setUser(mapToDTO(employeeDetails));
		return jwtAuthenticationResposne;
	}

	private EmployeeDto convertToDto(EmployeeEntity employeeEntity) {
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

	public JwtAuthenticationResponse refreshToken(RefreshTokenRequest refreshTokenRequest) {
		String username = jwtService.extractUserName(refreshTokenRequest.getToken());
		EmployeeEntity employee = employeeRepository.findByEmail(username).orElseThrow();
		if (jwtService.isTokenValid(refreshTokenRequest.getToken(), employee)) {
			var jwt = jwtService.generateToken(employee);
			JwtAuthenticationResponse jwtAuthenticationResposne = new JwtAuthenticationResponse();
			jwtAuthenticationResposne.setToken(jwt);
			jwtAuthenticationResposne.setRefreshToken(refreshTokenRequest.getToken());
			return jwtAuthenticationResposne;
		}
		return null;
	}
}
