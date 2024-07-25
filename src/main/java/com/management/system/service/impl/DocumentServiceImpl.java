package com.management.system.service.impl;

import java.io.IOException;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.management.system.entity.DocumentEnity;
import com.management.system.repository.DocumentRepository;
import com.management.system.service.DocumentService;
import com.management.system.service.DocumentTypeCount;

@Service
public class DocumentServiceImpl implements DocumentService {

	private final DocumentRepository documentRepository;

	public DocumentServiceImpl(DocumentRepository documentRepository) {
		this.documentRepository = documentRepository;
	}

	@Override
	public DocumentEnity saveDocument(MultipartFile file, Long userId) {
		try {
			if (!file.getContentType().equals("application/pdf") && !file.getContentType().startsWith("image/")) {
				throw new IllegalArgumentException("Invalid file type.");
			}
			if (file.getSize() > 2 * 1024 * 1024) { // 2 MB
				throw new IllegalArgumentException("File size exceeds limit.");
			}

			DocumentEnity document = new DocumentEnity();
			document.setName(file.getOriginalFilename());
			document.setFile(file.getBytes());
			document.setType(file.getContentType());
			document.setUserId(userId);

			return documentRepository.save(document);
		} catch (IOException e) {
			throw new RuntimeException("Could not store file. Please try again.", e);
		}
	}

	@Override
	public List<DocumentEnity> getAllDocuments() {
		return documentRepository.findAll();
	}

	@Override
	public DocumentEnity getDocumentById(Long id) {
		return documentRepository.findById(id).orElseThrow(() -> new RuntimeException("Document not found"));
	}

	@Override
	public byte[] getDocumentFile(Long id) {
		DocumentEnity document = getDocumentById(id);
		return document.getFile();
	}

	
	public List<DocumentTypeCount> getDocumentCountsGroupedByType() {
		return documentRepository.countDocumentsGroupedByType();
	}

	@Override
	public List<DocumentEnity> getDocuments(Integer userId) {
		return documentRepository.findByUserId(userId);
	}
}