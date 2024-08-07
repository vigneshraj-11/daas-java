package com.management.system.dto;

public class PageSummary {

	private Long totalFiles;
	private String weekday;

	public PageSummary(Long totalFiles, String weekday) {
		this.totalFiles = totalFiles;
		this.weekday = weekday;
	}

	public Long getTotalFiles() {
		return totalFiles;
	}

	public void setTotalFiles(Long totalFiles) {
		this.totalFiles = totalFiles;
	}

	public String getWeekday() {
		return weekday;
	}

	public void setWeekday(String weekday) {
		this.weekday = weekday;
	}

}
