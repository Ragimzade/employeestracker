package com.employeestracker.service.impl;

import com.employeestracker.dto.ProjectDto;
import com.employeestracker.entity.ProjectEntity;
import com.employeestracker.exception.NotFoundException;
import com.employeestracker.mapper.ProjectMapper;
import com.employeestracker.repository.ProjectRepository;
import com.employeestracker.service.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;
    private final ProjectMapper projectMapper;

    @Override
    public ProjectEntity createProject(ProjectDto projectDto) {
        ProjectEntity projectEntity = projectMapper.toProjectEntity(projectDto);
        return projectRepository.save(projectEntity);
    }

    @Override
    public void deleteProject(long id) {
        ProjectEntity project = getProject(id);
        projectRepository.delete(project);
    }

    @Override
    public ProjectEntity getProject(long id) {
        return projectRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Project with id '%s' not found".formatted(id)));
    }

    @Override
    public ProjectEntity updateProject(ProjectDto projectDto, Long projectId) {
        ProjectEntity projectEntity = getProject(projectId);
        projectMapper.updateProjectEntityFromDto(projectDto, projectEntity);
        return projectRepository.save(projectEntity);
    }
}
