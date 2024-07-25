package com.management.system.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.management.system.repository.DocumentRepository;
import com.management.system.repository.EmployeeRepository;

@Service
public class CountService {

	@Autowired
	private EmployeeRepository employeeRepository;

	@Autowired
	private DocumentRepository documentRepository;

	public long getTotalEmployeeCount() {
		return employeeRepository.count();
	}

	public long getTotalDocumentCount() {
		return documentRepository.countTotalDocuments();
	}

	public long getCountByType(String type) {
		return documentRepository.countDocumentsByType(type);
	}
	
}