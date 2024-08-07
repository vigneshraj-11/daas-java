package com.management.system.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.multipart.MultipartFile;

import com.management.system.dto.BatchDTO;
import com.management.system.dto.EmployeeDto;
import com.management.system.dto.OrganizationDTO;
import com.management.system.entity.BatchEntity;
import com.management.system.entity.EmployeeEntity;
import com.management.system.entity.FileEntity;
import com.management.system.entity.OrganizationEntity;
import com.management.system.repository.BatchRepository;
import com.management.system.repository.EmployeeRepository;

@Service
public class FileUploadService {

	@Autowired
	private BatchRepository batchRepository;

	@Autowired
	private EmployeeRepository employeeRepository;

	private final String rootDir = System.getProperty("os.name").toLowerCase().contains("win") ? "C:/DAaaS"
			: "/home/user/DAaaS";
	private final long MAX_FILE_SIZE = 50 * 1024 * 1024; // 50 MB

	@Transactional
	public BatchEntity saveFiles(MultipartFile[] files, String batchName, Integer orgId, Long userId)
			throws IOException {
		if (files == null || files.length == 0) {
			throw new IllegalArgumentException("At least one file must be provided.");
		}

		for (MultipartFile file : files) {
			if (file.getSize() > MAX_FILE_SIZE) {
				throw new MultipartException(
						"File size exceeds the maximum limit of 50 MB: " + file.getOriginalFilename());
			}
		}

		File batchDir = new File(rootDir + File.separator + batchName);
		if (!batchDir.exists()) {
			batchDir.mkdirs();
		}

		Optional<BatchEntity> existingBatchOpt = batchRepository.findByBatchName(batchName);
		BatchEntity batch;
		List<FileEntity> fileDetailsList;

		if (existingBatchOpt.isPresent()) {
			batch = existingBatchOpt.get();
			fileDetailsList = batch.getFileDetails();
			batch.setUpdatedDate(LocalDateTime.now());
			batch.setUpdatedUserId(userId);
			batch.setOrganizationId(orgId);
		} else {
			batch = new BatchEntity();
			batch.setBatchName(batchName);
			batch.setBatchDirectory(batchDir.getAbsolutePath());
			batch.setCreatedDate(LocalDateTime.now());
			batch.setCreatedUserId(userId);
			batch.setOrganizationId(orgId);
			fileDetailsList = new ArrayList<>();
		}

		for (MultipartFile file : files) {
			Path filePath = Paths.get(batchDir.getAbsolutePath(), file.getOriginalFilename());
			if (Files.exists(filePath)) {
				updateFileCounts(batch, filePath);
			} else {
				if (file.getContentType().equals("application/zip")) {
					fileDetailsList.addAll(extractAndSaveZip(batchDir, file, batch));
				} else {
					FileEntity fileDetails = saveFile(batchDir, file, batch);
					fileDetailsList.add(fileDetails);
					updateFileCounts(batch, fileDetails);
				}
			}
		}

		batch.setTotalFiles(fileDetailsList.size());
		batch.setFileDetails(fileDetailsList);

		batchRepository.save(batch);

		return batch;
	}

	private void updateFileCounts(BatchEntity batch, Path filePath) throws IOException {
		if (Files.probeContentType(filePath).startsWith("image")) {
			batch.setTotalPages(batch.getTotalPages() + 1);
		} else if (Files.probeContentType(filePath).equals("application/pdf")) {
			batch.setTotalPages(batch.getTotalPages() + getPdfPageCount(filePath));
		}
	}

	private void updateFileCounts(BatchEntity batch, FileEntity fileDetails) {
		if ("image".equals(fileDetails.getFileType())) {
			batch.setTotalImages(batch.getTotalImages() + 1);
			batch.setTotalPages(batch.getTotalPages() + 1);
		} else if ("pdf".equals(fileDetails.getFileType())) {
			batch.setTotalPdfs(batch.getTotalPdfs() + 1);
			batch.setTotalPages(batch.getTotalPages() + fileDetails.getPages());
		}
	}

	private FileEntity saveFile(File batchDir, MultipartFile file, BatchEntity batch) throws IOException {
		Path filePath = Paths.get(batchDir.getAbsolutePath(), file.getOriginalFilename());

		// Replace the existing file if it already exists
		Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

		FileEntity fileDetails = new FileEntity();
		fileDetails.setFileName(file.getOriginalFilename());
		fileDetails.setBatch(batch);

		if (file.getContentType().startsWith("image")) {
			fileDetails.setFileType("image");
			fileDetails.setPages(1);
		} else if (file.getContentType().equals("application/pdf")) {
			fileDetails.setFileType("pdf");
			fileDetails.setPages(getPdfPageCount(file));
		}

		return fileDetails;
	}

	private List<FileEntity> extractAndSaveZip(File batchDir, MultipartFile zipFile, BatchEntity batch)
			throws IOException {
		List<FileEntity> extractedFiles = new ArrayList<>();

		try (ZipInputStream zis = new ZipInputStream(zipFile.getInputStream())) {
			ZipEntry zipEntry;
			while ((zipEntry = zis.getNextEntry()) != null) {
				if (!zipEntry.isDirectory()) {
					Path filePath = Paths.get(batchDir.getAbsolutePath(), zipEntry.getName());
					if (Files.exists(filePath)) {
						// If file already exists, just update the page count and skip further
						// processing
						updateFileCounts(batch, filePath);
					} else {
						Files.copy(zis, filePath, StandardCopyOption.REPLACE_EXISTING);

						FileEntity fileDetails = new FileEntity();
						fileDetails.setFileName(zipEntry.getName());
						fileDetails.setBatch(batch);

						if (Files.probeContentType(filePath).startsWith("image")) {
							fileDetails.setFileType("image");
							fileDetails.setPages(1);
						} else if (Files.probeContentType(filePath).equals("application/pdf")) {
							fileDetails.setFileType("pdf");
							fileDetails.setPages(getPdfPageCount(filePath));
						}

						extractedFiles.add(fileDetails);
						updateFileCounts(batch, fileDetails);
					}
				}
				zis.closeEntry();
			}
		}

		return extractedFiles;
	}

	private int getPdfPageCount(MultipartFile pdfFile) throws IOException {
		try (PDDocument document = PDDocument.load(pdfFile.getInputStream())) {
			return document.getNumberOfPages();
		}
	}

	private int getPdfPageCount(Path pdfFilePath) throws IOException {
		try (PDDocument document = PDDocument.load(pdfFilePath.toFile())) {
			return document.getNumberOfPages();
		}
	}

	public BatchDTO getBatchDetails(String batchName) {
		BatchEntity batch = batchRepository.findByBatchName(batchName)
				.orElseThrow(() -> new RuntimeException("Batch not found"));

		BatchDTO dto = new BatchDTO();
		dto.setBatchName(batch.getBatchName());
		dto.setBatchDirectory(batch.getBatchDirectory());
		dto.setTotalFiles(batch.getTotalFiles());
		dto.setTotalImages(batch.getTotalImages());
		dto.setTotalPDF(batch.getTotalPdfs());
		dto.setTotalPages(batch.getTotalPages());
		dto.setCreatedDate(batch.getCreatedDate());
		dto.setFileDetails(batch.getFileDetails().stream().map(file -> {
			BatchDTO.FileDTO fileDTO = new BatchDTO.FileDTO();
			fileDTO.setFileName(file.getFileName());
			fileDTO.setFileType(file.getFileType());
			fileDTO.setPages(file.getPages());
			return fileDTO;
		}).collect(Collectors.toList()));
		return dto;
	}

	public List<BatchDTO> getBatchDetailsByUserId(Integer userId) {
		List<BatchEntity> batches = batchRepository.getBatchDetailsByCreatedUserId(userId);
		return convertToBatchDTOList(batches);
	}

	public List<BatchDTO> getBatchDetailsByOrganization(Integer userId) {
		OrganizationEntity organization = employeeRepository.findById(userId)
				.orElseThrow(() -> new RuntimeException("User not found")).getOrganization();
		List<BatchEntity> batches = batchRepository.getBatchDetailsByCreatedUserId(organization.getId());
		return convertToBatchDTOList(batches);
	}

	public List<BatchDTO> getAllBatchDetails() {
		List<BatchEntity> batches = batchRepository.findAll();
		return convertToBatchDTOList(batches);
	}

	private List<BatchDTO> convertToBatchDTOList(List<BatchEntity> batches) {
		return batches.stream().map(batch -> {
			BatchDTO dto = new BatchDTO();
			dto.setBatchName(batch.getBatchName());
			dto.setBatchDirectory(batch.getBatchDirectory());
			dto.setCreatedDate(batch.getCreatedDate());
			dto.setTotalFiles(batch.getTotalFiles());
			dto.setTotalImages(batch.getTotalImages());
			dto.setTotalPDF(batch.getTotalPdfs());
			dto.setTotalPages(batch.getTotalPages());
			dto.setFileDetails(batch.getFileDetails().stream().map(file -> {
				BatchDTO.FileDTO fileDTO = new BatchDTO.FileDTO();
				fileDTO.setFileName(file.getFileName());
				fileDTO.setFileType(file.getFileType());
				fileDTO.setPages(file.getPages());
				return fileDTO;
			}).collect(Collectors.toList()));

			Optional<EmployeeEntity> employeeEntityOpt = employeeRepository
					.findById(batch.getCreatedUserId().intValue());
			employeeEntityOpt.ifPresent(employeeEntity -> {
				dto.setCreatedBy(convertToEmployeeDto(employeeEntity));
			});

			return dto;
		}).collect(Collectors.toList());
	}

	private EmployeeDto convertToEmployeeDto(EmployeeEntity employee) {
		EmployeeDto dto = new EmployeeDto();
		dto.setId(employee.getId());
		dto.setUserNumber(employee.getEmployeeNumber());
		dto.setFullName(employee.getFullName());
		dto.setEmail(employee.getEmail());
		dto.setAuthority(employee.getRole().toString());
		dto.setDepartment(employee.getDepartment());
		dto.setDestination(employee.getDestination());
		dto.setActiveStatus(employee.getActiveStatus());
		dto.setFirstName(employee.getFirstName());
		dto.setLastName(employee.getLastName());
		dto.setAddress(employee.getAddress());
		dto.setGender(employee.getGender());
		dto.setMobileNumber(employee.getMobileNumber());
		dto.setProfilePicture(employee.getProfilePicture());
		dto.setOrganization(convertToOrganizationDto(employee.getOrganization()));
		return dto;
	}

	private OrganizationDTO convertToOrganizationDto(OrganizationEntity organization) {
		OrganizationDTO dto = new OrganizationDTO();
		dto.setId(organization.getId());
		dto.setOrganizationName(organization.getOrganizationName());
		return dto;
	}

}