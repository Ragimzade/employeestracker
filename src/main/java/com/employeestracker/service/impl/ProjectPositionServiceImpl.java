package com.employeestracker.service.impl;

import com.employeestracker.dto.ProjectPositionDto;
import com.employeestracker.entity.ProjectEntity;
import com.employeestracker.entity.ProjectPositionEntity;
import com.employeestracker.entity.UserEntity;
import com.employeestracker.exception.NotFoundException;
import com.employeestracker.mapper.ProjectPositionMapper;
import com.employeestracker.repository.ProjectPositionRepository;
import com.employeestracker.service.ProjectPositionService;
import com.employeestracker.service.ProjectService;
import com.employeestracker.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class ProjectPositionServiceImpl implements ProjectPositionService {

    private final ProjectPositionRepository projectPositionRepository;
    private final UserService userService;
    private final ProjectService projectService;
    private final ProjectPositionMapper mapper;

    @Override
    public ProjectPositionEntity createProjectPosition(ProjectPositionDto projectPositionDto) {
        UserEntity user = userService.getUser(projectPositionDto.getUserId());
        ProjectEntity project = projectService.getProject(projectPositionDto.getProjectId());
        ProjectPositionEntity projectPosition = mapper.toProjectPositionEntity(projectPositionDto, user, project);
        return projectPositionRepository.save(projectPosition);
    }

    @Override
    public void deleteProjectPosition(long id) {
        ProjectPositionEntity projectPosition = getProjectPosition(id);
        projectPositionRepository.delete(projectPosition);
    }

    @Override
    public ProjectPositionEntity getProjectPosition(long id) {
        return projectPositionRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Project position with id '%s' not found".formatted(id)));
    }

    @Override
    public ProjectPositionEntity updateProjectPosition(ProjectPositionDto projectPositionDto, Long id) {
        ProjectPositionEntity projectPositionEntity = getProjectPosition(id);

        UserEntity user = userService.getUser(projectPositionDto.getUserId());
        ProjectEntity project = projectService.getProject(projectPositionDto.getProjectId());
        mapper.updateProjectPositionEntityFromDto(projectPositionDto, projectPositionEntity, user, project);

        return projectPositionRepository.save(projectPositionEntity);
    }

    @Override
    public ProjectPositionEntity getUserActiveProjectPosition(long userId) {
        return projectPositionRepository.findActiveProjectPositionByUserId(userId, LocalDate.now());
    }
}
