package com.employeestracker.service.impl;

import com.employeestracker.dto.DepartmentDto;
import com.employeestracker.entity.DepartmentEntity;
import com.employeestracker.exception.AlreadyExistsException;
import com.employeestracker.exception.NotFoundException;
import com.employeestracker.repository.DepartmentRepository;
import com.employeestracker.service.DepartmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class DepartmentServiceImpl implements DepartmentService {

    private final DepartmentRepository departmentRepository;

    @Override
    public DepartmentEntity createDepartment(DepartmentDto department) {
        departmentRepository.findDepartmentEntityByTitle(department.getTitle()).ifPresent(u -> {
            throw new AlreadyExistsException("Department '%s' already exists".formatted(department.getTitle()));
        });

        DepartmentEntity entity = DepartmentEntity.builder()
                .title(department.getTitle())
                .build();

        return departmentRepository.save(entity);
    }

    @Override
    public void deleteDepartment(long id) {
        DepartmentEntity departmentEntity = getDepartment(id);
        departmentRepository.delete(departmentEntity);
    }

    @Override
    public List<DepartmentEntity> getAllDepartments() {
        return (List<DepartmentEntity>) departmentRepository.findAll();
    }

    @Override
    public DepartmentEntity getDepartment(long id) {
        return departmentRepository.findDepartmentEntityById(id)
                .orElseThrow(() -> new NotFoundException("Department with id '%s' not found".formatted(id)));
    }

    @Override
    public DepartmentEntity updateDepartment(DepartmentDto departmentDto, Long id) {
        DepartmentEntity departmentEntity = getDepartment(id);
        departmentEntity.setTitle(departmentDto.getTitle());
        return departmentRepository.save(departmentEntity);
    }
}
