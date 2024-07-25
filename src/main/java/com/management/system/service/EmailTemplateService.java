package com.management.system.service;

import com.management.system.entity.EmailTemplate;
import com.management.system.entity.EmployeeEntity;
import com.management.system.repository.EmailTemplateRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import java.util.Map;
import java.util.Optional;

@Service
public class EmailTemplateService {

	@Autowired
	private EmailTemplateRepository emailTemplateRepository;

	@Autowired
	private JavaMailSender mailSender;

	@Autowired
	private TemplateEngine templateEngine;

	@Value("${spring.mail.username}")
	private String fromEmail;

	@Value("${mail.sender.name}")
	private String senderName;

	public void sendEmail(String templateName, String to, String subject, Map<String, Object> variables) {
		Optional<EmailTemplate> templateOptional = emailTemplateRepository.findByTemplateName(templateName);
		if (templateOptional.isPresent()) {
			EmailTemplate template = templateOptional.get();

			MimeMessage mimeMessage = mailSender.createMimeMessage();
			try {
				MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);

				Context context = new Context();
				context.setVariables(variables);

				String html = templateEngine.process(template.getBody(), context);

				helper.setTo(to);
				helper.setFrom(String.format("%s <%s>", senderName, fromEmail));
				helper.setSubject(subject);
				helper.setText(html, true);

				mailSender.send(mimeMessage);

			} catch (MessagingException e) {
				e.printStackTrace();
			}
		}
	}

	public void sendWelcomeEmail(EmployeeEntity employee, String employeeNumber, String rawPassword) {
		Optional<EmailTemplate> templateOptional = emailTemplateRepository.findByTemplateName("welcome_email");
		if (templateOptional.isPresent()) {
			EmailTemplate template = templateOptional.get();

			MimeMessage mimeMessage = mailSender.createMimeMessage();
			try {
				MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);

				Context context = new Context();
				context.setVariable("fullName", employee.getFullName());
				context.setVariable("employeeNumber", employeeNumber);
				context.setVariable("email", employee.getEmail());
				context.setVariable("password", rawPassword);
				context.setVariable("senderName", senderName);

				String html = templateEngine.process(template.getBody(), context);

				helper.setTo(employee.getEmail());
				helper.setSubject(template.getSubject());
				helper.setFrom(String.format("%s <%s>", senderName, fromEmail));
				helper.setText(html, true);

				mailSender.send(mimeMessage);

			} catch (MessagingException e) {
				e.printStackTrace();
			}
		}
	}

}