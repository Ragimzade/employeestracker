package com.employeestracker.service.impl;

import com.employeestracker.dto.DepartmentDto;
import com.employeestracker.entity.DepartmentEntity;
import com.employeestracker.exception.AlreadyExistsException;
import com.employeestracker.exception.NotFoundException;
import com.employeestracker.repository.DepartmentRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@ContextConfiguration(classes = {DepartmentServiceImpl.class})
class DepartmentServiceImplTest {

    @MockBean
    private DepartmentRepository departmentRepository;
    @Autowired
    private DepartmentServiceImpl departmentServiceImpl;
    private static DepartmentEntity departmentEntity;
    private static DepartmentDto departmentDto;

    @BeforeAll
    static void setUp() {
        departmentEntity = DepartmentEntity.builder()
                .id(1L)
                .title("Department 1")
                .build();

        departmentDto = new DepartmentDto();
        departmentDto.setTitle("Department 1");
    }

    @Test
    void createDepartment_Success() {
        when(departmentRepository.findDepartmentEntityByTitle(departmentDto.getTitle()))
                .thenReturn(Optional.empty());
        when(departmentRepository.save(any(DepartmentEntity.class)))
                .thenReturn(departmentEntity);

        DepartmentEntity createdDepartment = departmentServiceImpl.createDepartment(departmentDto);

        assertNotNull(createdDepartment);
        assertEquals(createdDepartment.getId(), departmentEntity.getId());
        assertEquals(createdDepartment.getTitle(), departmentEntity.getTitle());
    }

    @Test
    void createDepartment_AlreadyExists() {
        when(departmentRepository.findDepartmentEntityByTitle(departmentDto.getTitle()))
                .thenReturn(Optional.of(departmentEntity));
        assertThrows(AlreadyExistsException.class, () -> departmentServiceImpl.createDepartment(departmentDto));
    }

    @Test
    void deleteDepartment_Success() {
        when(departmentRepository.findDepartmentEntityById(1L))
                .thenReturn(Optional.of(departmentEntity));

        departmentServiceImpl.deleteDepartment(1L);

        verify(departmentRepository, times(1)).delete(departmentEntity);
    }

    @Test
    void deleteDepartment_NotFound() {
        when(departmentRepository.findDepartmentEntityById(1L))
                .thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> departmentServiceImpl.deleteDepartment(1L));
    }

    @Test
    void getAllDepartments_Success() {
        List<DepartmentEntity> departments = Collections.singletonList(departmentEntity);
        when(departmentRepository.findAll())
                .thenReturn(departments);

        List<DepartmentEntity> allDepartments = departmentServiceImpl.getAllDepartments();

        assertNotNull(allDepartments);
        assertEquals(allDepartments.size(), departments.size());
        assertEquals(allDepartments, departments);
    }

    @Test
    void getDepartment_Success() {
        when(departmentRepository.findDepartmentEntityById(1L))
                .thenReturn(Optional.of(departmentEntity));

        DepartmentEntity returnedDepartment = departmentServiceImpl.getDepartment(1L);

        assertNotNull(returnedDepartment);
        assertEquals(returnedDepartment.getId(), departmentEntity.getId());
        assertEquals(returnedDepartment.getTitle(), departmentEntity.getTitle());
    }
}