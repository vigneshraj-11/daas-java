package com.management.system.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.management.system.entity.EmployeeEntity;
import com.management.system.enumcollection.Role;

@Repository
public interface EmployeeRepository extends CrudRepository<EmployeeEntity, Integer> {

	Optional<EmployeeEntity> findByEmail(String email);

	EmployeeEntity findByRole(Role role);
	
	long countByRole(Role role);

	@Query("SELECT MAX(e.employeeNumber) FROM EmployeeEntity e")
	String findMaxEmployeeNumber();

	@Query("SELECT e FROM EmployeeEntity e where e.fullName Like %:query% or e.email Like %:query%")
	public List<EmployeeEntity> searchEmployee(String query);

	List<EmployeeEntity> findByEmailNot(String currentUserEmail);

	List<EmployeeEntity> findByEmailNotAndActiveStatus(String email, String activeStatus);

	@Query("SELECT e FROM EmployeeEntity e WHERE e.email <> :email AND e.activeStatus = :activeStatus AND e.role <> 2")
	List<EmployeeEntity> findByEmailNotAndActiveStatusAndNotRoleSuperadmin(@Param("email") String email,
			@Param("activeStatus") String activeStatus);
	
}