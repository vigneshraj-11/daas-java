package com.management.system.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.management.system.dto.CreateEmployee;
import com.management.system.dto.EmployeeDto;
import com.management.system.dto.JwtAuthenticationResponse;
import com.management.system.dto.Login;
import com.management.system.dto.RefreshTokenRequest;
import com.management.system.dto.ResponseDTO;
import com.management.system.service.AuthenticationService;
import com.management.system.service.ForgotPasswordService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("${endpoints}/authentication")
public class AuthenticationController {

	private final AuthenticationService authenticationService;

	@Autowired
	private ForgotPasswordService forgotPasswordService;

	public AuthenticationController(AuthenticationService authenticationService) {
		this.authenticationService = authenticationService;
	}

	@Operation(summary = "Create User", description = "This API used for create a new User.")
	@Tag(name = "Authentication")
	@PostMapping("/createUser")
	public ResponseEntity<ResponseDTO> createEmployee(@RequestBody CreateEmployee createEmployee) {
		EmployeeDto employee = authenticationService.createEmployee(createEmployee);
		ResponseDTO responseDTO = new ResponseDTO("Success", "User created successfully", employee);
		return ResponseEntity.ok(responseDTO);
	}

	@Operation(summary = "Login", description = "This API used for login to get new web tokens.")
	@Tag(name = "Authentication")
	@PostMapping("/login")
	public ResponseEntity<JwtAuthenticationResponse> login(@RequestBody Login login) throws IllegalAccessException {
		return ResponseEntity.ok(authenticationService.login(login));
	}

	@Operation(summary = "Refresh Token", description = "This API used for get new web token by using existing refresh token.")
	@Tag(name = "Authentication")
	@PostMapping("/refreshToken")
	public ResponseEntity<ResponseDTO> refreshToken(@RequestBody RefreshTokenRequest refreshTokenRequest) {
		JwtAuthenticationResponse jwtResponse = authenticationService.refreshToken(refreshTokenRequest);
		ResponseDTO responseDTO = new ResponseDTO("Success", "Token refreshed successfully", jwtResponse);
		return ResponseEntity.ok(responseDTO);
	}

	@Operation(summary = "Initiate Password Reset", description = "Initiates the password reset process by sending an OTP to the user's email.")
	@Tag(name = "Authentication")
	@PostMapping("/forgotPassword/initiate")
	public ResponseEntity<ResponseDTO> initiatePasswordReset(@RequestParam("email") String email) {
		forgotPasswordService.initiatePasswordReset(email);
		ResponseDTO responseDTO = new ResponseDTO("Success",
				"Password reset initiated. Check your email for further instructions", null);
		return ResponseEntity.ok(responseDTO);
	}

	@Operation(summary = "Verify OTP", description = "Verifies the OTP provided by the user.")
	@Tag(name = "Authentication")
	@PostMapping("/forgotPassword/verify")
	public ResponseEntity<?> verifyOTP(@RequestParam("email") String email, @RequestParam("otp") String otp) {
		if (forgotPasswordService.verifyOTP(email, otp)) {
			ResponseDTO responseDTO = new ResponseDTO("Success",
					"OTP verified successfully. New Password has been sent to your mail.", null);
			return ResponseEntity.ok(responseDTO);
		} else {
			ResponseDTO responseDTO = new ResponseDTO("Failed", "Invalid OTP or OTP expired. Please try again.", null);
			return ResponseEntity.status(HttpStatus.REQUEST_TIMEOUT).body(responseDTO);
		}
	}

	@Operation(summary = "Change Password", description = "Resets the user's password.")
	@Tag(name = "Authentication")
	@PostMapping("/changePassword")
	public ResponseEntity<ResponseDTO> resetPassword(@RequestParam("email") String email,
			@RequestParam("oldPassword") String oldPassword, @RequestParam("newPassword") String newPassword) {
		try {
			String result = forgotPasswordService.resetPassword(email, oldPassword, newPassword);
			if ("Password changed".equals(result)) {
				ResponseDTO responseDTO = new ResponseDTO("Success", "Password reset successfully", null);
				return ResponseEntity.ok(responseDTO);
			} else {
				ResponseDTO responseDTO = new ResponseDTO("Failed", result, null);
				return ResponseEntity.badRequest().body(responseDTO);
			}
		} catch (Exception e) {
			ResponseDTO responseDTO = new ResponseDTO("Error", "An error occurred while resetting the password.", null);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseDTO);
		}
	}

}
