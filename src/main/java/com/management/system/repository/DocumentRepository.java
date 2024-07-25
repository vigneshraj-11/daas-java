package com.management.system.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.management.system.entity.DocumentEnity;
import com.management.system.service.DocumentTypeCount;

public interface DocumentRepository extends JpaRepository<DocumentEnity, Long> {

	@Query("SELECT COUNT(d) FROM DocumentEnity d")
	long countTotalDocuments();

	@Query("SELECT COUNT(d) FROM DocumentEnity d WHERE d.type = :type")
	long countDocumentsByType(@Param("type") String type);

	@Query("SELECT d.type as type, COUNT(d) as count FROM DocumentEnity d GROUP BY d.type")
	List<DocumentTypeCount> countDocumentsGroupedByType();

	Optional<DocumentEnity> findById(Long id);

	List<DocumentEnity> findByUserId(Integer userId);

}
