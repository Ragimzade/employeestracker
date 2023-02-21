package com.employeestracker.service.impl;

import com.employeestracker.dto.ProjectDto;
import com.employeestracker.entity.ProjectEntity;
import com.employeestracker.exception.NotFoundException;
import com.employeestracker.mapper.ProjectMapper;
import com.employeestracker.repository.ProjectRepository;
import com.employeestracker.service.ProjectService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@SpringBootTest
@ContextConfiguration(classes = {ProjectServiceImpl.class})
public class ProjectServiceTest {

    @Autowired
    private ProjectService projectService;

    @MockBean
    private ProjectRepository repository;
    @MockBean
    private ProjectMapper mapper;

    private ProjectEntity projectEntity;

    @BeforeEach
    void setUp() {
        projectEntity = new ProjectEntity();
        projectEntity.setId(1L);
    }

    @Test
    void createProject_success() {
        ProjectDto projectDto = new ProjectDto();
        projectDto.setTitle("test");
        projectDto.setStartDate(LocalDate.now());
        projectDto.setEndDate(LocalDate.now());

        when(mapper.toProjectEntity(projectDto)).thenReturn(projectEntity);
        when(repository.save(projectEntity)).thenReturn(projectEntity);

        ProjectEntity createProject = projectService.createProject(projectDto);
        assertEquals(projectEntity, createProject);
    }

    @Test
    void deleteProject_success() {
        when(repository.findById(projectEntity.getId())).thenReturn(Optional.of(projectEntity));
        projectService.deleteProject(projectEntity.getId());
        verify(repository, times(1)).delete(projectEntity);
    }

    @Test
    void getProject_success() {
        when(repository.findById(projectEntity.getId())).thenReturn(Optional.of(projectEntity));
        ProjectEntity foundProject = projectService.getProject(projectEntity.getId());
        assertEquals(projectEntity.getId(), foundProject.getId());
    }

    @Test
    void getProject_not_found() {
        long projectId = 1L;
        when(repository.findById(projectEntity.getId())).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> projectService.getProject(projectId));
    }

}
