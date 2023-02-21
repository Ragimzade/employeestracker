package com.employeestracker.service;

import com.employeestracker.dto.DepartmentDto;
import com.employeestracker.entity.DepartmentEntity;

import java.util.List;

public interface DepartmentService {

    DepartmentEntity createDepartment(DepartmentDto department);

    void deleteDepartment(long id);

    DepartmentEntity getDepartment(long id);

    DepartmentEntity updateDepartment(DepartmentDto department, Long departmentId);

    List<DepartmentEntity> getAllDepartments();

}
