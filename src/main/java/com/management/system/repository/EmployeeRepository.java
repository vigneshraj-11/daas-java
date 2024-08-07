package com.management.system.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.management.system.entity.EmployeeEntity;
import com.management.system.entity.OrganizationEntity;
import com.management.system.enumcollection.Role;

@Repository
public interface EmployeeRepository extends CrudRepository<EmployeeEntity, Integer> {

	Optional<EmployeeEntity> findByEmail(String email);

	EmployeeEntity findByRole(Role role);

	long countByRole(Role role);

	@Query("SELECT e.employeeNumber FROM EmployeeEntity e ORDER BY e.id DESC")
	List<String> findMaxEmployeeNumber();

	@Query("SELECT e FROM EmployeeEntity e where e.fullName Like %:query% or e.email Like %:query%")
	public List<EmployeeEntity> searchEmployee(String query);

	List<EmployeeEntity> findByEmailNot(String currentUserEmail);

	List<EmployeeEntity> findByEmailNotAndActiveStatus(String email, String activeStatus);

	@Query("SELECT e FROM EmployeeEntity e WHERE e.email <> :email AND e.activeStatus = :activeStatus AND e.role <> 2")
	List<EmployeeEntity> findByEmailNotAndActiveStatusAndNotRoleSuperadmin(@Param("email") String email,
			@Param("activeStatus") String activeStatus);

	String getRoleById(Integer userId);

	@Query("SELECT COUNT(e) FROM EmployeeEntity e WHERE e.role <> 2")
	long countEmployeesExcludingSuperAdmin();

	@Query("SELECT COUNT(e) FROM EmployeeEntity e WHERE e.organization = :organization")
	long countEmployeeByOrgID(@Param("organization") OrganizationEntity organization);

}