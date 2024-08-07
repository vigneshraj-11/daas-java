package com.management.system.entity;

import jakarta.persistence.*;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.management.system.encryption.AppEncryption;
import com.management.system.enumcollection.Role;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Date;
import java.util.List;

@Table(name = "employees_details")
@Entity
public class EmployeeEntity implements UserDetails {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false)
	private Integer id;

	@Column(nullable = false, unique = true)
	private String employeeNumber;

	@Column(nullable = false)
	private String fullName;

	@Column(nullable = false)
	private String firstName;

	@Column(nullable = false)
	private String lastName;

	@Column(nullable = true)
	private String address;

	@Column(nullable = true)
	private String gender;

	@Column(nullable = true)
	private String mobileNumber;

	@Lob
	private byte[] profilePicture;

	@Column(nullable = false)
	private Integer createdBy;

	@Column(unique = true, length = 100, nullable = false)
	private String email;

	@Column(nullable = false)
	private String password;

	@Column(nullable = true)
	private String department;

	@Column(nullable = true)
	private String destination;

	@CreationTimestamp
	@Column(updatable = false, name = "created_at")
	private Date createdAt;

	@UpdateTimestamp
	@Column(name = "updated_at")
	private Date updatedAt;

	private String resetPasswordToken;

	private LocalDateTime resetPasswordTokenExpiry;

	private Role role;

	@Column(nullable = false)
	private String activeStatus = "Active";

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

	public String getEmployeeNumber() {
		return decryptString(employeeNumber);
	}

	public void setEmployeeNumber(String employeeNumber) {
		this.employeeNumber = encryptString(employeeNumber);
	}

	public String getFullName() {
		return decryptString(fullName);
	}

	public void setFullName(String fullName) {
		this.fullName = encryptString(fullName);
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getDepartment() {
		return decryptString(department);
	}

	public void setDepartment(String department) {
		this.department = encryptString(department);
	}

	public String getDestination() {
		return decryptString(destination);
	}

	public void setDestination(String destination) {
		this.destination = encryptString(destination);
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public Date getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(Date updatedAt) {
		this.updatedAt = updatedAt;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return List.of(new SimpleGrantedAuthority(role.name()));
	}

	public String getPassword() {
		return password;
	}

	@Override
	public String getUsername() {
		return email;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public String getResetPasswordToken() {
		return resetPasswordToken;
	}

	public void setResetPasswordToken(String resetPasswordToken) {
		this.resetPasswordToken = resetPasswordToken;
	}

	public LocalDateTime getResetPasswordTokenExpiry() {
		return resetPasswordTokenExpiry;
	}

	public void setResetPasswordTokenExpiry(LocalDateTime resetPasswordTokenExpiry) {
		this.resetPasswordTokenExpiry = resetPasswordTokenExpiry;
	}

	public String getFirstName() {
		return decryptString(firstName);
	}

	public void setFirstName(String firstName) {
		this.firstName = encryptString(firstName);
	}

	public String getLastName() {
		return decryptString(lastName);
	}

	public void setLastName(String lastName) {
		this.lastName = encryptString(lastName);
	}

	public String getAddress() {
		return decryptString(address);
	}

	public void setAddress(String address) {
		this.address = encryptString(address);
	}

	public String getGender() {
		return decryptString(gender);
	}

	public void setGender(String gender) {
		this.gender = encryptString(gender);
	}

	public String getMobileNumber() {
		return decryptString(mobileNumber);
	}

	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = encryptString(mobileNumber);
	}

	public byte[] getProfilePicture() {
		return decryptBytes(profilePicture);
	}

	public void setProfilePicture(byte[] profilePicture) {
		this.profilePicture = encryptBytes(profilePicture);
	}

	public Integer getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
	}

	public String getActiveStatus() {
		return activeStatus;
	}

	public void setActiveStatus(String activeStatus) {
		this.activeStatus = activeStatus;
	}

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