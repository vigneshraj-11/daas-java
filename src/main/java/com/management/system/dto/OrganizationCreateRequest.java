package com.management.system.dto;

public class OrganizationCreateRequest {

	private String organizationName;
	private byte[] organizationLogo;

	public String getOrganizationName() {
		return organizationName;
	}

	public void setOrganizationName(String organizationName) {
		this.organizationName = organizationName;
	}

	public byte[] getOrganizationLogo() {
		return organizationLogo;
	}

	public void setOrganizationLogo(byte[] organizationLogo) {
		this.organizationLogo = organizationLogo;
	}
}
