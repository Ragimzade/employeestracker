package com.employeestracker.service;

import com.employeestracker.dto.ProjectDto;
import com.employeestracker.entity.ProjectEntity;

public interface ProjectService {

    ProjectEntity createProject(ProjectDto projectDto);

    void deleteProject(long id);

    ProjectEntity getProject(long id);

    ProjectEntity updateProject(ProjectDto projectDto, Long projectId);
}
