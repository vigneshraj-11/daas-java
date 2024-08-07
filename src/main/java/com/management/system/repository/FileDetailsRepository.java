package com.management.system.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.management.system.entity.FileEntity;

public interface FileDetailsRepository extends JpaRepository<FileEntity, Long> {
}
