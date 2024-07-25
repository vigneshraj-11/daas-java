package com.management.system.repository;

import com.management.system.entity.EmailTemplate;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface EmailTemplateRepository extends JpaRepository<EmailTemplate, Integer> {
    Optional<EmailTemplate> findByTemplateName(String templateName);
}