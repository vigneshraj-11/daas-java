package com.management.system.service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.management.system.entity.EmployeeEntity;
import com.management.system.repository.EmployeeRepository;
import com.management.system.utils.PasswordGenerator;

@Service
public class ForgotPasswordService {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EmailTemplateService emailTemplateService;

    @Value("${mail.sender.name}")
    private String senderName;

    public void initiatePasswordReset(String username) {
        Optional<EmployeeEntity> userOptional = employeeRepository.findByEmail(username);
        if (userOptional.isPresent()) {
            EmployeeEntity user = userOptional.get();
            String otp = generateOTP();
            user.setResetPasswordToken(otp);
            user.setResetPasswordTokenExpiry(LocalDateTime.now().plusMinutes(5));
            employeeRepository.save(user);
            sendPasswordResetEmail(user.getEmail(), user.getFullName(), otp);
        }
    }

    public boolean verifyOTP(String username, String otp) {
        Optional<EmployeeEntity> userOptional = employeeRepository.findByEmail(username);
        if (userOptional.isPresent()) {
            EmployeeEntity user = userOptional.get();
            String resetPasswordToken = user.getResetPasswordToken();
            if (resetPasswordToken != null && resetPasswordToken.equals(otp)
                    && user.getResetPasswordTokenExpiry().isAfter(LocalDateTime.now())) {
                String newPassword = PasswordGenerator.generateRandomPassword();
                user.setPassword(passwordEncoder.encode(newPassword));
                user.setResetPasswordToken(null);
                user.setResetPasswordTokenExpiry(null);
                employeeRepository.save(user);
                String subject = senderName + " - " + user.getFullName() + "'s New Password";
                Map<String, Object> variables = new HashMap<>();
                variables.put("fullName", user.getFullName());
                variables.put("newPassword", newPassword);
                variables.put("senderName", senderName);

                emailTemplateService.sendEmail("password_reset_confirmation", user.getEmail(), subject, variables);
                return true;
            }
        }
        return false;
    }

    public void resetPassword(String username, String newPassword) {
        Optional<EmployeeEntity> userOptional = employeeRepository.findByEmail(username);
        if (userOptional.isPresent()) {
            EmployeeEntity user = userOptional.get();
            user.setPassword(passwordEncoder.encode(newPassword));
            user.setResetPasswordToken(null);
            user.setResetPasswordTokenExpiry(null);
            employeeRepository.save(user);
            String subject = "Password Reset Confirmation";
            Map<String, Object> variables = new HashMap<>();
            variables.put("fullName", user.getFullName());
            variables.put("senderName", senderName);

            emailTemplateService.sendEmail("password_reset_confirmation", user.getEmail(), subject, variables);
        }
    }

    private void sendPasswordResetEmail(String to, String fullName, String otp) {
        String subject = "Password Reset Request";
        Map<String, Object> variables = new HashMap<>();
        variables.put("fullName", fullName);
        variables.put("otp", otp);
        variables.put("senderName", senderName);

        emailTemplateService.sendEmail("password_reset", to, subject, variables);
    }

    private String generateOTP() {
        Random random = new Random();
        int otp = 100000 + random.nextInt(900000);
        return String.valueOf(otp);
    }
}