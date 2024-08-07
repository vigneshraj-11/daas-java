package com.management.system.dto;

import java.time.LocalDateTime;
import java.util.List;

public class BatchDTO {

	private String batchName;
	private String batchDirectory;
	private Integer totalPages;
	private Integer totalFiles;
	private Integer totalImages;
	private Integer totalPDF;
	private LocalDateTime createdDate;
	private List<FileDTO> fileDetails;
	private EmployeeDto createdBy;

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

	public LocalDateTime getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(LocalDateTime createdDate) {
		this.createdDate = createdDate;
	}

	public List<FileDTO> getFileDetails() {
		return fileDetails;
	}

	public void setFileDetails(List<FileDTO> fileDetails) {
		this.fileDetails = fileDetails;
	}

	public static class FileDTO {
		private String fileName;
		private String fileType;
		private int pages;

		public String getFileName() {
			return fileName;
		}

		public void setFileName(String fileName) {
			this.fileName = fileName;
		}

		public String getFileType() {
			return fileType;
		}

		public void setFileType(String fileType) {
			this.fileType = fileType;
		}

		public int getPages() {
			return pages;
		}

		public void setPages(int pages) {
			this.pages = pages;
		}

	}

	public EmployeeDto getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(EmployeeDto createdBy) {
		this.createdBy = createdBy;
	}

	public Integer getTotalPages() {
		return totalPages;
	}

	public void setTotalPages(Integer totalPages) {
		this.totalPages = totalPages;
	}

	public Integer getTotalFiles() {
		return totalFiles;
	}

	public void setTotalFiles(Integer totalFiles) {
		this.totalFiles = totalFiles;
	}

	public Integer getTotalImages() {
		return totalImages;
	}

	public void setTotalImages(Integer totalImages) {
		this.totalImages = totalImages;
	}

	public Integer getTotalPDF() {
		return totalPDF;
	}

	public void setTotalPDF(Integer totalPDF) {
		this.totalPDF = totalPDF;
	}

}