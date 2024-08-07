package com.management.system.controller;

import com.management.system.dto.OrganizationCreateRequest;
import com.management.system.entity.OrganizationEntity;
import com.management.system.service.OrganizationService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("${endpoints}/org")
public class OrganizationController {

	@Autowired
	private OrganizationService organizationService;

	@Operation(summary = "Create Organization", description = "Use this API used for create Organization.")
	@Tag(name = "Organization")
	@PostMapping("/createOrg")
	public OrganizationEntity createOrganization(@RequestParam("name") String name,
			@RequestParam("logo") MultipartFile logo) {
		try {
			return organizationService.createOrganization(name, logo);
		} catch (IOException e) {
			throw new RuntimeException("Failed to create organization", e);
		}
	}

}