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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@SpringBootTest
@ContextConfiguration(classes = {ProjectPositionServiceImpl.class})
class ProjectPositionServiceTest {

    @Autowired
    private ProjectPositionService projectPositionService;
    @MockBean
    private ProjectPositionRepository projectPositionRepository;
    @MockBean
    private UserService userService;
    @MockBean
    private ProjectService projectService;
    @MockBean
    private ProjectPositionMapper mapper;
    private ProjectPositionEntity projectPosition;

    @BeforeEach
    void setUp() {
        projectPosition = new ProjectPositionEntity();
        projectPosition.setId(1L);
    }

    @Test
    void createProjectPosition_success() {
        UserEntity user = new UserEntity();
        user.setId(1L);

        ProjectEntity project = new ProjectEntity();
        project.setId(2L);

        ProjectPositionDto projectPositionDto = new ProjectPositionDto();
        projectPositionDto.setUserId(user.getId());
        projectPositionDto.setProjectId(project.getId());

        when(userService.getUser(user.getId())).thenReturn(user);
        when(projectService.getProject(project.getId())).thenReturn(project);
        when(mapper.toProjectPositionEntity(projectPositionDto, user, project)).thenReturn(projectPosition);
        when(projectPositionRepository.save(projectPosition)).thenReturn(projectPosition);

        ProjectPositionEntity createdProjectPosition = projectPositionService.createProjectPosition(projectPositionDto);
        assertEquals(projectPosition, createdProjectPosition);
    }

    @Test
    void deleteProjectPosition_success() {
        when(projectPositionRepository.findById(projectPosition.getId())).thenReturn(Optional.of(projectPosition));
        projectPositionService.deleteProjectPosition(projectPosition.getId());
        verify(projectPositionRepository, times(1)).delete(projectPosition);
    }

    @Test
    void getProjectPosition_success() {
        when(projectPositionRepository.findById(projectPosition.getId())).thenReturn(Optional.of(projectPosition));
        ProjectPositionEntity foundProjectPosition = projectPositionService.getProjectPosition(projectPosition.getId());
        assertEquals(projectPosition.getId(), foundProjectPosition.getId());
    }

    @Test
    void getProjectPosition_not_found() {
        long projectId = 1L;
        when(projectPositionRepository.findById(projectId)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> projectPositionService.getProjectPosition(projectId));
    }
}
