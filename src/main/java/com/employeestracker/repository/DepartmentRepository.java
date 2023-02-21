package com.employeestracker.repository;

import com.employeestracker.entity.DepartmentEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DepartmentRepository extends CrudRepository<DepartmentEntity, Long> {

    Optional<DepartmentEntity> findDepartmentEntityByTitle(String title);

    Optional<DepartmentEntity> findDepartmentEntityById(long id);
}
