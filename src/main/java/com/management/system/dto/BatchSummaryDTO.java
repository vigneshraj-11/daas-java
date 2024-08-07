package com.management.system.dto;

public class BatchSummaryDTO {

    private Long totalFiles;
    private Long totalPages;

    public BatchSummaryDTO(Long totalFiles, Long totalPages) {
        this.totalFiles = totalFiles;
        this.totalPages = totalPages;
    }

    public Long getTotalFiles() {
        return totalFiles;
    }

    public void setTotalFiles(Long totalFiles) {
        this.totalFiles = totalFiles;
    }

    public Long getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(Long totalPages) {
        this.totalPages = totalPages;
    }
}
