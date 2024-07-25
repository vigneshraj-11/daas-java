package com.management.system.config;

import org.springframework.context.annotation.Configuration;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.security.SecuritySchemes;
import io.swagger.v3.oas.annotations.servers.Server;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;

@Configuration
@OpenAPIDefinition(
    info = @Info(
        title = "Centralized Management System (CMS) Rest Services",
        version = "1.0.0",
        contact = @Contact(name = "Vignesh Gunasekaran", email = "vn@ctdtechs.com", url = "https://web.yammer.com/main/org/ctdtechs.com/users/eyJfdHlwZSI6IlVzZXIiLCJpZCI6IjE4MDEyNDU1NDAzNTIifQ/storyline"),
        license = @License(name = "Apache 2.0", url = "https://www.apache.org/licenses/LICENSE-2.0"),
        description = "The Centralized Management System (CMS) API encompasses comprehensive modules for HRMS, DMS, CRM, and PMS. " +
                      "This API provides a secure, centralized platform for managing human resources, documents, customer relationships, and projects. " +
                      "Users authenticate securely with their credentials before accessing the API's features, ensuring data integrity and security. " +
                      "Detailed features include employee management, document storage and retrieval, customer interaction tracking, and project planning and execution. " +
                      "Additionally, users can generate customized reports for each module by specifying parameters like date ranges, ensuring comprehensive insights and analytics. " +
                      "These reports, available in various formats, provide valuable data for decision-making and operational efficiency. " +
                      "With its robust management capabilities and secure authentication, this API serves as a comprehensive solution for modern business management needs."
    ),
    servers = {
        @Server(url = "http://localhost:8080/daas", description = "Local Server"),
        @Server(url = "http://localhost:8090/daas", description = "Local Live Server")
    },
    security = {@SecurityRequirement(name = "bearerToken")}
)
@SecuritySchemes({
    @SecurityScheme(
        name = "bearerToken",
        type = SecuritySchemeType.HTTP,
        scheme = "bearer",
        bearerFormat = "JWT"
    )
})
public class OpenApiConfig {
}