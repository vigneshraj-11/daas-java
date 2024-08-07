package com.management.system.service;

import com.management.system.dto.CreateEmployee;
import com.management.system.dto.EmployeeDto;
import com.management.system.dto.JwtAuthenticationResponse;
import com.management.system.dto.Login;
import com.management.system.dto.RefreshTokenRequest;

public interface AuthenticationService {

	EmployeeDto createEmployee(CreateEmployee createEmployee);
	
	JwtAuthenticationResponse login(Login login) throws IllegalAccessException;
	
	JwtAuthenticationResponse refreshToken(RefreshTokenRequest refreshTokenRequest);
}
