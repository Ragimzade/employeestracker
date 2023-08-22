package com.employeestracker.mapper;

import com.employeestracker.dto.UserDto;
import com.employeestracker.dto.response.UserResponse;
import com.employeestracker.entity.DepartmentEntity;
import com.employeestracker.entity.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {


    @Mapping(target = "id", ignore = true)
    @Mapping(target = "department", source = "departmentEntity")
    UserEntity toUserEntity(UserDto userDto, DepartmentEntity departmentEntity);

    @Mapping(target = "department", source = "userEntity.department.title")
    UserResponse toUserResponse(UserEntity userEntity);


    @Mapping(target = "jobTitle", source = "userDto.jobTitle")
    @Mapping(target = "password", source = "userDto.password")
    @Mapping(target = "email", source = "userDto.email")
    @Mapping(target = "firstName", source = "userDto.firstName")
    @Mapping(target = "lastName", source = "userDto.lastName")
    @Mapping(target = "department", source = "department")
    @Mapping(target = "id", ignore = true)
    void updateUserEntityFromDto(UserDto userDto, @MappingTarget UserEntity user, DepartmentEntity department);

}
