package com.management.system.service;

import com.management.system.dto.CreateEmployee;
import com.management.system.dto.JwtAuthenticationResponse;
import com.management.system.dto.Login;
import com.management.system.dto.RefreshTokenRequest;
import com.management.system.entity.EmployeeEntity;

public interface AuthenticationService {

	EmployeeEntity createEmployee(CreateEmployee createEmployee);
	
	JwtAuthenticationResponse login(Login login) throws IllegalAccessException;
	
	JwtAuthenticationResponse refreshToken(RefreshTokenRequest refreshTokenRequest);
}
