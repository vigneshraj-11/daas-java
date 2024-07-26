package com.management.system.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.management.system.dto.CreateEmployee;
import com.management.system.dto.JwtAuthenticationResponse;
import com.management.system.dto.Login;
import com.management.system.dto.RefreshTokenRequest;
import com.management.system.entity.EmployeeEntity;
import com.management.system.service.AuthenticationService;
import com.management.system.service.ForgotPasswordService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("${endpoints}/auth")
public class AuthenticationController {

	private final AuthenticationService authenticationService;

	@Autowired
	private ForgotPasswordService forgotPasswordService;


	public AuthenticationController(AuthenticationService authenticationService) {
		this.authenticationService = authenticationService;
	}

	@Operation(summary = "Server Test", description = "This API used for Test server is work or not.")
	@Tag(name = "Auth")
	@GetMapping("/servertest")
	public String serverTest() {
		return "Server working fine...";
	}
	
	@Operation(summary = "Create Employee", description = "This API used for create a new Employee.")
	@Tag(name = "Auth")
	@PostMapping("/createEmployee")
	public ResponseEntity<EmployeeEntity> createEmployee(@RequestBody CreateEmployee createEmployee) {
		return ResponseEntity.ok(authenticationService.createEmployee(createEmployee));
	}

	@Operation(summary = "Login", description = "This API used for login to get new web tokens.")
	@Tag(name = "Auth")
	@PostMapping("/login")
	public ResponseEntity<JwtAuthenticationResponse> login(@RequestBody Login login) throws IllegalAccessException {
		return ResponseEntity.ok(authenticationService.login(login));
	}

	@Operation(summary = "Refresh Token", description = "This API used for get new web token by using existing refresh token.")
	@Tag(name = "Auth")
	@PostMapping("/refreshToken")
	public ResponseEntity<JwtAuthenticationResponse> refreshToken(
			@RequestBody RefreshTokenRequest refreshTokenRequest) {
		return ResponseEntity.ok(authenticationService.refreshToken(refreshTokenRequest));
	}

	@Operation(summary = "Initiate Password Reset", description = "Initiates the password reset process by sending an OTP to the user's email.")
	@Tag(name = "Auth")
	@PostMapping("/reset/initiate")
	public ResponseEntity<String> initiatePasswordReset(@RequestParam("email") String email) {
		forgotPasswordService.initiatePasswordReset(email);
		return ResponseEntity.ok("Password reset initiated. Check your email for further instructions.");
	}

	@Operation(summary = "Verify OTP", description = "Verifies the OTP provided by the user.")
	@Tag(name = "Auth")
	@PostMapping("/reset/verify")
	public ResponseEntity<String> verifyOTP(@RequestParam("email") String email, @RequestParam("otp") String otp) {
		if (forgotPasswordService.verifyOTP(email, otp)) {
			return ResponseEntity.ok("OTP verified successfully. New Password has sent to your mail.");
		} else {
			return ResponseEntity.badRequest().body("Invalid OTP or OTP expired. Please try again.");
		}
	}

	@Operation(summary = "Reset Password", description = "Resets the user's password.")
	@Tag(name = "Auth")
	@PostMapping("/reset/confirm")
	public ResponseEntity<String> resetPassword(@RequestParam("email") String email,
			@RequestParam("newPassword") String newPassword) {
		forgotPasswordService.resetPassword(email, newPassword);
		return ResponseEntity.ok("Password reset successfully. Check your email for confirmation.");
	}

}
