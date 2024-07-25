package com.management.system.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.management.system.entity.DocumentEnity;
import com.management.system.repository.DocumentRepository;
import com.management.system.service.DocumentService;
import com.management.system.service.DocumentTypeCount;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("${endpoints}/document")
public class DocumentController {

	private final DocumentService documentService;

	@Autowired
	private final DocumentRepository documentRepository;

	public DocumentController(DocumentService documentService, DocumentRepository documentRepository) {
		this.documentService = documentService;
		this.documentRepository = documentRepository;
	}

	@Operation(summary = "Upload a document", description = "Uploads a document for a specific user. The document is saved as a BLOB in the database.")
	@Tag(name = "Document")
	@PostMapping("/upload")
	public ResponseEntity<DocumentEnity> uploadDocument(@RequestParam("file") MultipartFile file,
			@RequestParam("userId") Long userId) {

		try {
			DocumentEnity document = documentService.saveDocument(file, userId);
			return new ResponseEntity<>(document, HttpStatus.CREATED);
		} catch (IllegalArgumentException e) {
			return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
		}
	}

	@Operation(summary = "Retrieve all documents", description = "Fetches a list of all documents stored in the database.")
	@Tag(name = "Document")
	@GetMapping("/all")
	public ResponseEntity<List<DocumentEnity>> getAllDocuments() {
		List<DocumentEnity> documents = documentService.getAllDocuments();
		return new ResponseEntity<>(documents, HttpStatus.OK);
	}

	@Operation(summary = "Retrieve all documents", description = "Fetches a list of all documents stored in the database.")
	@Tag(name = "Document")
	@GetMapping("/getDocument")
	public ResponseEntity<List<DocumentEnity>> getDocuments(@RequestParam Integer userId) {
		List<DocumentEnity> documents = documentService.getDocuments(userId);
		return new ResponseEntity<>(documents, HttpStatus.OK);
	}

	@Operation(summary = "Retrieve a document by ID", description = "Fetches the details of a specific document by its ID.")
	@Tag(name = "Document")
	@GetMapping("/{id}")
	public ResponseEntity<DocumentEnity> getDocumentById(@PathVariable Long id) {
		DocumentEnity document = documentService.getDocumentById(id);
		return new ResponseEntity<>(document, HttpStatus.OK);
	}

	@Operation(summary = "Download a document file", description = "Downloads the file data of a specific document by its ID. The file is retrieved as a binary stream.")
	@Tag(name = "Document")
	@GetMapping("/file/{id}")
	public ResponseEntity<byte[]> getDocumentFile(@PathVariable Long id) {
		byte[] fileData = documentService.getDocumentFile(id);
		DocumentEnity document = documentService.getDocumentById(id);
		HttpHeaders headers = new HttpHeaders();
		headers.set(HttpHeaders.CONTENT_TYPE, document.getType());
		headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + document.getName() + "\"");
		return new ResponseEntity<>(fileData, headers, HttpStatus.OK);
	}

	@GetMapping("/counts")
	public List<DocumentTypeCount> getDocumentCountsGroupedByType() {
		return documentService.getDocumentCountsGroupedByType();
	}

	@Operation(summary = "Delete Employee", description = "This API is used to set the active status of an employee to 'Inactive' using their employee ID.")
	@Tag(name = "document")
	@PostMapping("/delete")
	public DocumentEnity deleteDocument(@RequestParam Long id) {
		DocumentEnity document = documentRepository.findById(id).orElseThrow();
		document.setDocStatus("Unavailable");
		return documentRepository.save(document);
	}

}