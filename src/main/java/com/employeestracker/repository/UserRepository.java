package com.employeestracker.repository;

import com.employeestracker.entity.DepartmentEntity;
import com.employeestracker.entity.UserEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<UserEntity, Long> {
    Optional<UserEntity> findByEmail(String email);

    Optional<UserEntity> findById(long id);

    @Query("""
            SELECT u
            FROM UserEntity u
            LEFT JOIN u.projectPositions pp
            WHERE pp IS NULL
            OR pp.startDate BETWEEN :startDate AND :endDate
            OR pp.endDate BETWEEN :startDate AND :endDate
            """)
    List<UserEntity> findAvailableUsers(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    List<UserEntity> findAllByDepartment(DepartmentEntity department);
}
