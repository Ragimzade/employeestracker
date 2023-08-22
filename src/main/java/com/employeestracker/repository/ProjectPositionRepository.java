package com.employeestracker.repository;

import com.employeestracker.entity.ProjectPositionEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.Optional;

public interface ProjectPositionRepository extends CrudRepository<ProjectPositionEntity, Long> {

    Optional<ProjectPositionEntity> findById(long id);

    @Query("SELECT p FROM ProjectPositionEntity p WHERE p.users.id = :userId AND " +
            "(p.startDate <= :currentDate AND p.endDate >= :currentDate)")
    ProjectPositionEntity findActiveProjectPositionByUserId(@Param("userId") Long userId,
                                                                     @Param("currentDate") LocalDate currentDate);


}
