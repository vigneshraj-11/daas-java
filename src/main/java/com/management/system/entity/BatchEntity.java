package com.management.system.entity;

import java.time.LocalDateTime;
import java.util.List;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Table(name = "batch_details")
@Entity
public class BatchEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String batchName;
	private String batchDirectory;
	private int totalFiles;
	private int totalImages;
	private int totalPdfs;
	private int totalPages;
	private LocalDateTime createdDate;
	private LocalDateTime updatedDate;
	private Long createdUserId;
	private Long updatedUserId;
	private Integer organizationId;

	@OneToMany(mappedBy = "batch", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
	private List<FileEntity> fileDetails;

	// Getters and Setters

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getBatchName() {
		return batchName;
	}

	public void setBatchName(String batchName) {
		this.batchName = batchName;
	}

	public String getBatchDirectory() {
		return batchDirectory;
	}

	public void setBatchDirectory(String batchDirectory) {
		this.batchDirectory = batchDirectory;
	}

	public int getTotalFiles() {
		return totalFiles;
	}

	public void setTotalFiles(int totalFiles) {
		this.totalFiles = totalFiles;
	}

	public int getTotalImages() {
		return totalImages;
	}

	public void setTotalImages(int totalImages) {
		this.totalImages = totalImages;
	}

	public int getTotalPdfs() {
		return totalPdfs;
	}

	public void setTotalPdfs(int totalPdfs) {
		this.totalPdfs = totalPdfs;
	}

	public int getTotalPages() {
		return totalPages;
	}

	public void setTotalPages(int totalPages) {
		this.totalPages = totalPages;
	}

	public LocalDateTime getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(LocalDateTime createdDate) {
		this.createdDate = createdDate;
	}

	public LocalDateTime getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(LocalDateTime updatedDate) {
		this.updatedDate = updatedDate;
	}

	public Long getCreatedUserId() {
		return createdUserId;
	}

	public void setCreatedUserId(Long createdUserId) {
		this.createdUserId = createdUserId;
	}

	public Long getUpdatedUserId() {
		return updatedUserId;
	}

	public void setUpdatedUserId(Long updatedUserId) {
		this.updatedUserId = updatedUserId;
	}

	public List<FileEntity> getFileDetails() {
		return fileDetails;
	}

	public void setFileDetails(List<FileEntity> fileDetails) {
		this.fileDetails = fileDetails;
	}

	public Integer getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(Integer organizationId) {
		this.organizationId = organizationId;
	}

}