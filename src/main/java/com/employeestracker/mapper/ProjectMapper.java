package com.employeestracker.mapper;

import com.employeestracker.dto.ProjectDto;
import com.employeestracker.dto.response.ProjectResponse;
import com.employeestracker.entity.ProjectEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ProjectMapper {

    @Mapping(target = "id", ignore = true)
    ProjectEntity toProjectEntity(ProjectDto projectDto);

    ProjectResponse toProjectResponse(ProjectEntity project);

    void updateProjectEntityFromDto(ProjectDto projectDto, @MappingTarget ProjectEntity entity);

}
