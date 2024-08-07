package com.management.system.service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.management.system.dto.BatchSummaryDTO;
import com.management.system.entity.BatchEntity;
import com.management.system.entity.OrganizationEntity;
import com.management.system.repository.BatchRepository;
import com.management.system.repository.EmployeeRepository;

@Service
public class BatchService {

	@Autowired
	private BatchRepository batchRepository;

	@Autowired
	private EmployeeRepository employeeRepository;

	public BatchSummaryDTO getBatchSummaryByUserId(Integer userId) {
		return batchRepository.getBatchSummaryByUserId(userId);
	}

	public BatchSummaryDTO getBatchSummary() {
		return batchRepository.getBatchSummary();
	}

	public BatchSummaryDTO getBatchSummaryByOrgId(Integer userId) {
		OrganizationEntity organization = employeeRepository.findById(userId)
				.orElseThrow(() -> new RuntimeException("User not found")).getOrganization();
		return batchRepository.getBatchSummaryByOrgId(organization.getId());
	}

	public Map<String, Long> getCurrentWeekPageSummary() {
		LocalDateTime startOfWeek = LocalDate.now().with(DayOfWeek.MONDAY).atStartOfDay();
		LocalDateTime endOfWeek = startOfWeek.plusDays(7);

		List<BatchEntity> batches = batchRepository.findBatchesWithinWeek(startOfWeek, endOfWeek);

		Map<String, Long> weeklySummary = new LinkedHashMap<>();
		for (DayOfWeek day : DayOfWeek.values()) {
			weeklySummary.put(day.name(), 0L);
		}

		for (BatchEntity batch : batches) {
			String dayOfWeek = batch.getCreatedDate().getDayOfWeek().name();
			weeklySummary.put(dayOfWeek, weeklySummary.get(dayOfWeek) + batch.getTotalPages());
		}

		return weeklySummary;
	}

	public Map<String, Long> getCurrentWeekPageSummaryByOrgId(Integer userId) {
		LocalDateTime startOfWeek = LocalDate.now().with(DayOfWeek.MONDAY).atStartOfDay();
		LocalDateTime endOfWeek = startOfWeek.plusDays(7);
		OrganizationEntity organization = employeeRepository.findById(userId)
				.orElseThrow(() -> new RuntimeException("User not found")).getOrganization();
		List<BatchEntity> batches = batchRepository.findBatchesWithinWeekOrgID(startOfWeek, endOfWeek,
				organization.getId());

		Map<String, Long> weeklySummary = new LinkedHashMap<>();
		for (DayOfWeek day : DayOfWeek.values()) {
			weeklySummary.put(day.name(), 0L);
		}

		for (BatchEntity batch : batches) {
			String dayOfWeek = batch.getCreatedDate().getDayOfWeek().name();
			weeklySummary.put(dayOfWeek, weeklySummary.get(dayOfWeek) + batch.getTotalPages());
		}

		return weeklySummary;
	}

	public Map<String, Long> getCurrentWeekPageSummaryByUserId(Integer userId) {
		LocalDateTime startOfWeek = LocalDate.now().with(DayOfWeek.MONDAY).atStartOfDay();
		LocalDateTime endOfWeek = startOfWeek.plusDays(7);

		List<BatchEntity> batches = batchRepository.findBatchesWithinWeekUserID(startOfWeek, endOfWeek, userId);

		Map<String, Long> weeklySummary = new LinkedHashMap<>();
		for (DayOfWeek day : DayOfWeek.values()) {
			weeklySummary.put(day.name(), 0L);
		}

		for (BatchEntity batch : batches) {
			String dayOfWeek = batch.getCreatedDate().getDayOfWeek().name();
			weeklySummary.put(dayOfWeek, weeklySummary.get(dayOfWeek) + batch.getTotalPages());
		}

		return weeklySummary;
	}

}
