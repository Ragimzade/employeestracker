package com.employeestracker.service;

import com.employeestracker.dto.ProjectPositionDto;
import com.employeestracker.entity.ProjectPositionEntity;

public interface ProjectPositionService {

    ProjectPositionEntity createProjectPosition(ProjectPositionDto projectPositionDto);

    void deleteProjectPosition(long id);

    ProjectPositionEntity getProjectPosition(long id);

    ProjectPositionEntity updateProjectPosition(ProjectPositionDto projectPositionDto, Long projectPositionId);

    ProjectPositionEntity getUserActiveProjectPosition(long userId);
}
