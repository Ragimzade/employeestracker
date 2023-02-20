package com.employeestracker.mapper;

import com.employeestracker.dto.response.DepartmentResponse;
import com.employeestracker.entity.DepartmentEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;


@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface DepartmentMapper {

    DepartmentResponse toDepartmentResponse(DepartmentEntity departmentEntity);
}
