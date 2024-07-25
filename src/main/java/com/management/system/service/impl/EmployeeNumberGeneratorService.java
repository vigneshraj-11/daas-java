package com.management.system.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.management.system.repository.EmployeeRepository;

@Service
public class EmployeeNumberGeneratorService {
	
	@Autowired
    private EmployeeRepository employeeRepository;

    @Value("${empnumvar}")
    private String employeeNumberPrefix;

    public String generateEmployeeNumber() {
        String maxEmployeeNumber = employeeRepository.findMaxEmployeeNumber();
        int nextNumber = 1;
        if (maxEmployeeNumber != null && maxEmployeeNumber.startsWith(employeeNumberPrefix)) {
            String numberPart = maxEmployeeNumber.substring(employeeNumberPrefix.length());
            nextNumber = Integer.parseInt(numberPart) + 1;
        }
        return employeeNumberPrefix + String.format("%04d", nextNumber);
    }
}