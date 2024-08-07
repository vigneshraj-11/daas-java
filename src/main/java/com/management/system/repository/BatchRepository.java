package com.management.system.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.management.system.dto.BatchSummaryDTO;
import com.management.system.dto.PageSummary;
import com.management.system.entity.BatchEntity;

public interface BatchRepository extends JpaRepository<BatchEntity, Long> {

	Optional<BatchEntity> findByBatchName(String batchName);

	@Query("SELECT b FROM BatchEntity b LEFT JOIN FETCH b.fileDetails WHERE b.batchName = :batchName")
	Optional<BatchEntity> findByBatchNameWithDetails(@Param("batchName") String batchName);

	@Query("SELECT b FROM BatchEntity b LEFT JOIN FETCH b.fileDetails WHERE b.createdUserId = :userId")
	List<BatchEntity> getBatchDetailsBycreatedUserId(@Param("userId") Integer userId);

	@Query("SELECT b FROM BatchEntity b LEFT JOIN FETCH b.fileDetails WHERE b.createdUserId = :userId")
	List<BatchEntity> getBatchDetailsByCreatedUserId(@Param("userId") Integer userId);

	@Query("SELECT b FROM BatchEntity b LEFT JOIN FETCH b.fileDetails WHERE b.organizationId = :orgId")
	List<BatchEntity> getBatchDetailsByOrganizationId(@Param("orgId") Integer orgId);

	@Query("SELECT new com.management.system.dto.BatchSummaryDTO(SUM(b.totalFiles), SUM(b.totalPages)) "
			+ "FROM BatchEntity b WHERE b.createdUserId = :userId GROUP BY b.createdUserId")
	BatchSummaryDTO getBatchSummaryByUserId(@Param("userId") Integer userId);

	@Query("SELECT new com.management.system.dto.BatchSummaryDTO(SUM(b.totalFiles), SUM(b.totalPages)) FROM BatchEntity b")
	BatchSummaryDTO getBatchSummary();

	@Query("SELECT new com.management.system.dto.BatchSummaryDTO(SUM(b.totalFiles), SUM(b.totalPages)) "
			+ "FROM BatchEntity b WHERE b.organizationId = :id GROUP BY b.organizationId")
	BatchSummaryDTO getBatchSummaryByOrgId(Integer id);

	@Query("SELECT b FROM BatchEntity b WHERE b.createdDate >= :startOfWeek AND b.createdDate < :endOfWeek")
	List<BatchEntity> findBatchesWithinWeek(@Param("startOfWeek") LocalDateTime startOfWeek,
			@Param("endOfWeek") LocalDateTime endOfWeek);

	@Query("SELECT b FROM BatchEntity b WHERE b.createdDate >= :startOfWeek AND b.createdDate < :endOfWeek AND b.organizationId = :orgId")
	List<BatchEntity> findBatchesWithinWeekOrgID(@Param("startOfWeek") LocalDateTime startOfWeek,
			@Param("endOfWeek") LocalDateTime endOfWeek, @Param("orgId") Integer orgId);

	@Query("SELECT b FROM BatchEntity b WHERE b.createdDate >= :startOfWeek AND b.createdDate < :endOfWeek AND b.createdUserId = :userId")
	List<BatchEntity> findBatchesWithinWeekUserID(@Param("startOfWeek") LocalDateTime startOfWeek,
			@Param("endOfWeek") LocalDateTime endOfWeek, @Param("userId") Integer userId);

}