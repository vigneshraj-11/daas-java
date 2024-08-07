package com.management.system.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.management.system.encryption.AppEncryption;
import com.management.system.repository.EmployeeRepository;

@Service
public class EmployeeNumberGeneratorService {

	@Autowired
	private EmployeeRepository employeeRepository;

	@Value("${empnumvar}")
	private String employeeNumberPrefix;

	public String generateEmployeeNumber() {
		List<String> maxEmployeeNumbers = employeeRepository.findMaxEmployeeNumber();
		String maxEmployeeNumber = null;
		if (!maxEmployeeNumbers.isEmpty()) {
			maxEmployeeNumber = maxEmployeeNumbers.get(0);
		}

		if (maxEmployeeNumber != null) {
			maxEmployeeNumber = decryptString(maxEmployeeNumber);
		}

		int nextNumber = 1;
		if (maxEmployeeNumber != null && maxEmployeeNumber.startsWith(employeeNumberPrefix)) {
			String numberPart = maxEmployeeNumber.substring(employeeNumberPrefix.length());
			nextNumber = Integer.parseInt(numberPart) + 1;
		}

		return employeeNumberPrefix + String.format("%04d", nextNumber);
	}

	private String decryptString(String value) {
		try {
			return value != null ? AppEncryption.decrypt(value) : null;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}