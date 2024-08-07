package com.management.system.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.management.system.entity.OrganizationEntity;
import com.management.system.repository.DocumentRepository;
import com.management.system.repository.EmployeeRepository;

@Service
public class CountService {

	@Autowired
	private EmployeeRepository employeeRepository;

	@Autowired
	private DocumentRepository documentRepository;

	public long getTotalEmployeeCount() {
		return employeeRepository.countEmployeesExcludingSuperAdmin();
	}

	public long getTotalDocumentCountByUserId(Integer userId) {
		return documentRepository.countTotalDocumentsByUserId(userId);
	}

	public long getTotalDocumentCount() {
		return documentRepository.countTotalDocuments();
	}

	public long getCountByType(String type) {
		return documentRepository.countDocumentsByType(type);
	}

	public long getTotalEmployeeCount(Integer userId) {
		OrganizationEntity organization = employeeRepository.findById(userId)
				.orElseThrow(() -> new RuntimeException("User not found")).getOrganization();
		return employeeRepository.countEmployeeByOrgID(organization);
	}

}