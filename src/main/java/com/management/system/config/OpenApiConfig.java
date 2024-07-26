package com.management.system.config;

import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.security.SecuritySchemes;
import io.swagger.v3.oas.annotations.servers.Server;

@Configuration
@OpenAPIDefinition(info = @Info(title = "Document Automation as a Service (DAaS) Rest Services", version = "1.0.0", contact = @Contact(name = "Vignesh Gunasekaran", email = "vn@ctdtechs.com", url = "https://web.yammer.com/main/org/ctdtechs.com/users/eyJfdHlwZSI6IlVzZXIiLCJpZCI6IjE4MDEyNDU1NDAzNTIifQ/storyline"), license = @License(name = "Apache 2.0", url = "https://www.apache.org/licenses/LICENSE-2.0"), description = "The Document Automation as a Service (DAaS) API provides a secure and efficient platform for automating document management processes. "
		+ "This API allows users to manage documents, automate workflows, and generate reports with ease. "
		+ "Users authenticate securely with their credentials before accessing the API's features, ensuring data integrity and security. "
		+ "Detailed features include document storage and retrieval, automated document processing, and customizable reporting. "
		+ "Additionally, users can generate customized reports by specifying parameters like date ranges, providing comprehensive insights and analytics. "
		+ "These reports, available in various formats, offer valuable data for decision-making and operational efficiency. "
		+ "With its robust management capabilities and secure authentication, this API serves as a comprehensive solution for modern document automation needs."), servers = {
				@Server(url = "http://localhost:8080/daas", description = "Local Server"),
				@Server(url = "http://localhost:6060/daas", description = "Local Live Server") }, security = {
						@SecurityRequirement(name = "bearerToken") })
@SecuritySchemes({
		@SecurityScheme(name = "bearerToken", type = SecuritySchemeType.HTTP, scheme = "bearer", bearerFormat = "JWT") })
public class OpenApiConfig {
}