package com.employeestracker.mapper;


import com.employeestracker.dto.ProjectPositionDto;
import com.employeestracker.dto.response.ProjectPositionResponse;
import com.employeestracker.entity.ProjectEntity;
import com.employeestracker.entity.ProjectPositionEntity;
import com.employeestracker.entity.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ProjectPositionMapper {

    @Mapping(target = "startDate", source = "projectPositionDto.startDate")
    @Mapping(target = "endDate", source = "projectPositionDto.endDate")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "users", source = "user")
    @Mapping(target = "projects", source = "project")
    ProjectPositionEntity toProjectPositionEntity(ProjectPositionDto projectPositionDto, UserEntity user, ProjectEntity project);

    @Mapping(target = "entity.users", source = "user")
    @Mapping(target = "entity.projects", source = "project")
    @Mapping(target = "entity.startDate", source = "dto.startDate")
    @Mapping(target = "entity.endDate", source = "dto.endDate")
    @Mapping(target = "id", ignore = true)
    void updateProjectPositionEntityFromDto(ProjectPositionDto dto, @MappingTarget ProjectPositionEntity entity,
                                            UserEntity user, ProjectEntity project);

    @Mapping(target = "userId", source = "projectPositionEntity.users.id")
    @Mapping(target = "projectId", source = "projectPositionEntity.projects.id")
    ProjectPositionResponse toProjectPositionResponse(ProjectPositionEntity projectPositionEntity);
}
