package com.management.system.service.impl;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.management.system.dto.CreateEmployee;
import com.management.system.dto.JwtAuthenticationResponse;
import com.management.system.dto.Login;
import com.management.system.dto.RefreshTokenRequest;
import com.management.system.entity.EmployeeEntity;
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

	public EmployeeEntity createEmployee(CreateEmployee createEmployee) {
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
		String employeeNumber = employeeNumberGeneratorService.generateEmployeeNumber();
		employee.setEmployeeNumber(employeeNumber);
		String rawPassword = PasswordGenerator.generateRandomPassword();
		employee.setPassword(passwordEncoder.encode(rawPassword));
		EmployeeEntity savedEmployee = employeeRepository.save(employee);
		emailTemplateService.sendWelcomeEmail(savedEmployee, employeeNumber, rawPassword);
		return savedEmployee;
	}

	public JwtAuthenticationResponse login(Login login) throws IllegalAccessException {
		authenticationManager
				.authenticate(new UsernamePasswordAuthenticationToken(login.getEmail(), login.getPassword()));
		var employee = employeeRepository.findByEmail(login.getEmail())
				.orElseThrow(() -> new IllegalAccessException("Invalid Email or Password"));
		var jwt = jwtService.generateToken(employee);
		var refreshToken = jwtService.generateRefreshToken(new HashMap<>(), employee);
		JwtAuthenticationResponse jwtAuthenticationResposne = new JwtAuthenticationResponse();
		jwtAuthenticationResposne.setToken(jwt);
		jwtAuthenticationResposne.setRefreshToken(refreshToken);
		return jwtAuthenticationResposne;
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
