package com.management.system.dto;

public class JwtAuthenticationResponse {

	private EmployeeDto user;
	private String token;
	private String refreshToken;

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getRefreshToken() {
		return refreshToken;
	}

	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}

	public EmployeeDto getUser() {
		return user;
	}

	public void setUser(EmployeeDto user) {
		this.user = user;
	}

}