package com.management.system.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.management.system.dto.BatchDTO;
import com.management.system.entity.BatchEntity;
import com.management.system.entity.EmployeeEntity;
import com.management.system.service.EmployeeService;
import com.management.system.service.FileUploadService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("${endpoints}/fileuploader")
public class FileUploadController {

	@Autowired
	private FileUploadService fileUploadService;

	@Autowired
	private EmployeeService employeeService;

	@Operation(summary = "File Uploader", description = "Use this API for upload files")
	@Tag(name = "File Infromation")
	@PostMapping("/upload")
	public ResponseEntity<BatchEntity> uploadBatch(@RequestParam("files") MultipartFile[] files,
			@RequestParam("batchName") String batchName, @RequestParam("userId") Integer userId,
			@RequestParam("orgId") Long orgId) {
		try {
			BatchEntity batch = fileUploadService.saveFiles(files, batchName, userId, orgId);
			return ResponseEntity.ok(batch);
		} catch (IOException e) {
			return ResponseEntity.status(500).body(null);
		}
	}

	@Operation(summary = "Batch Details", description = "Use this API to get batch details by batch name")
	@Tag(name = "File Infromation")
	@GetMapping("/batch/{batchName}")
	public ResponseEntity<BatchDTO> getBatchDetails(@PathVariable String batchName) {
		try {
			BatchDTO batchDTO = fileUploadService.getBatchDetails(batchName);
			return ResponseEntity.ok(batchDTO);
		} catch (RuntimeException e) {
			return ResponseEntity.notFound().build();
		}
	}

	@Operation(summary = "Batch List Details", description = "Use this API to get batch list details by userId")
	@Tag(name = "File Infromation")
	@GetMapping("/getBatchList/{userId}")
	public ResponseEntity<List<BatchDTO>> getBatchListDetails(@PathVariable Integer userId) {
		try {
			EmployeeEntity employee = employeeService.findEmployeeById(userId);
			List<BatchDTO> batchDTOList;

			if (employee.getRole().toString().equals("SUPERADMIN")) {
				batchDTOList = fileUploadService.getAllBatchDetails();
			} else if (employee.getRole().toString().equals("ADMIN")) {
				batchDTOList = fileUploadService.getBatchDetailsByOrganization(userId);
			} else {
				batchDTOList = fileUploadService.getBatchDetailsByUserId(userId);
			}

			return ResponseEntity.ok(batchDTOList);
		} catch (RuntimeException e) {
			return ResponseEntity.notFound().build();
		}
	}

}