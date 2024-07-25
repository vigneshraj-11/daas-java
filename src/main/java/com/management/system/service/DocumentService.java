package com.management.system.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.management.system.entity.DocumentEnity;

public interface DocumentService {

	DocumentEnity saveDocument(MultipartFile file, Long userId);

	List<DocumentEnity> getAllDocuments();

	DocumentEnity getDocumentById(Long id);

	byte[] getDocumentFile(Long id);

	List<DocumentTypeCount> getDocumentCountsGroupedByType();

	List<DocumentEnity> getDocuments(Integer userId);
	
}