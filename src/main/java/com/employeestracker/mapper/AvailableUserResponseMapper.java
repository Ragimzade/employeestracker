package com.employeestracker.mapper;

import com.employeestracker.dto.response.AvailableUsersResponse;
import com.employeestracker.entity.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.time.LocalDate;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AvailableUserResponseMapper {


    @Mapping(target = "id", source = "userEntity.id")
    @Mapping(target = "firstName", source = "userEntity.firstName")
    @Mapping(target = "lastName", source = "userEntity.lastName")
    @Mapping(target = "email", source = "userEntity.email")
    @Mapping(target = "jobTitle", source = "userEntity.jobTitle")
    @Mapping(target = "department", source = "userEntity.department.id")
    @Mapping(target = "availableFrom", source = "from")
    @Mapping(target = "availableTo", source = "to")
    AvailableUsersResponse toAvailableUsersResponse(UserEntity userEntity, LocalDate from, LocalDate to);
}
