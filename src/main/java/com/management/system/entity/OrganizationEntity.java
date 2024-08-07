package com.management.system.entity;

import jakarta.persistence.*;

import com.management.system.encryption.AppEncryption;

import java.util.List;

@Table(name = "organizations_details")
@Entity
public class OrganizationEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false)
	private Integer id;

	@Column(nullable = false, unique = true)
	private String organizationName;

	@Lob
	@Column(columnDefinition = "LONGBLOB")
	private byte[] organizationLogo;

	@OneToMany(mappedBy = "organization", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<EmployeeEntity> employees;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "organization_id")
	private OrganizationEntity organization;

	public OrganizationEntity getOrganization() {
		return organization;
	}

	public void setOrganization(OrganizationEntity organization) {
		this.organization = organization;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getOrganizationName() {
		return decryptString(organizationName);
	}

	public void setOrganizationName(String organizationName) {
		this.organizationName = encryptString(organizationName);
	}

	public byte[] getOrganizationLogo() {
		return decryptBytes(organizationLogo);
	}

	public void setOrganizationLogo(byte[] organizationLogo) {
		this.organizationLogo = encryptBytes(organizationLogo);
	}

	public List<EmployeeEntity> getEmployees() {
		return employees;
	}

	public void setEmployees(List<EmployeeEntity> employees) {
		this.employees = employees;
	}

	// Encryption/Decryption methods
	private String encryptString(String value) {
		try {
			return value != null ? AppEncryption.encrypt(value) : null;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	private String decryptString(String value) {
		try {
			return value != null ? AppEncryption.decrypt(value) : null;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	private byte[] encryptBytes(byte[] value) {
		try {
			return value != null ? AppEncryption.encryptBytes(value) : null;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	private byte[] decryptBytes(byte[] value) {
		try {
			return value != null ? AppEncryption.decryptBytes(value) : null;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

}