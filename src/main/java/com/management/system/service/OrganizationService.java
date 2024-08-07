package com.management.system.service;

import com.management.system.entity.OrganizationEntity;
import com.management.system.repository.OrganizationRepository;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class OrganizationService {

	@Autowired
	private OrganizationRepository organizationRepository;

	public OrganizationEntity createOrganization(String name) {
		OrganizationEntity organization = new OrganizationEntity();
		organization.setOrganizationName(name);
		return organizationRepository.save(organization);
	}

	public OrganizationEntity createOrganization(String name, MultipartFile logo) throws IOException {
		OrganizationEntity organization = new OrganizationEntity();
		organization.setOrganizationName(name);
		organization.setOrganizationLogo(logo.getBytes());
		return organizationRepository.save(organization);
	}

}