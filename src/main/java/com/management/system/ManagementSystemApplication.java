package com.management.system;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.management.system.entity.EmployeeEntity;
import com.management.system.entity.OrganizationEntity;
import com.management.system.enumcollection.Role;
import com.management.system.repository.EmployeeRepository;
import com.management.system.service.EmailService;
import com.management.system.service.OrganizationService;
import com.management.system.utils.PasswordGenerator;

@SpringBootApplication
public class ManagementSystemApplication extends SpringBootServletInitializer implements CommandLineRunner {

	@Autowired
	public EmployeeRepository employeeRepository;
	@Autowired
	private OrganizationService organizationService;
	@Autowired
	private EmailService emailService;
	@Value("${mail.sender.name}")
	private String senderName;
	@Autowired
	private PasswordEncoder passwordEncoder;

	public static void main(String[] args) {
		SpringApplication.run(ManagementSystemApplication.class, args);
	}

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(ManagementSystemApplication.class);
	}

	@Override
	public void run(String... args) {
		long adminCount = employeeRepository.countByRole(Role.SUPERADMIN);
		if (adminCount == 0) {
			if (adminCount == 0) {
				OrganizationEntity organization = organizationService.createOrganization("CTD Techs");

				EmployeeEntity employee = new EmployeeEntity();
				employee.setEmployeeNumber("DAaS0001");
				employee.setFirstName("Vignesh");
				employee.setLastName("Gunasekaran");
				employee.setFullName("Vignesh Gunasekaran");
				employee.setEmail("vn@ctdtechs.com");
				employee.setRole(Role.SUPERADMIN);
				employee.setCreatedBy(1);
				String rawPassword = PasswordGenerator.generateRandomPassword();
				employee.setPassword(passwordEncoder.encode(rawPassword));
				employee.setOrganization(organization);
				EmployeeEntity savedEmployee = employeeRepository.save(employee);
				String subject = "Welcome to DAaS";
				String text = "Dear " + savedEmployee.getFullName() + ",\n\n"
						+ "Welcome to our DAaS! Your Super Admin account has been created successfully.\n\n"
						+ "Here are your login details:\n\n" + "Username: " + savedEmployee.getEmail() + "\n"
						+ "Password: " + rawPassword + "\n\n" + "Best regards,\n" + "" + senderName + "";
				emailService.sendSimpleEmail(savedEmployee.getEmail(), subject, text);
			}
		}
	}

	@Bean
	WebMvcConfigurer corsConfigurer() {
		return new WebMvcConfigurer() {
			@Override
			public void addCorsMappings(CorsRegistry registry) {
				registry.addMapping("/**").allowedOrigins("*").allowedMethods("*").exposedHeaders("*")
						.allowedHeaders("*");
			}
		};
	}
}
