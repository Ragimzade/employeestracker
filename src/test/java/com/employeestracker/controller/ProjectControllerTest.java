package com.employeestracker.controller;

import com.employeestracker.dto.ProjectDto;
import com.employeestracker.entity.ProjectEntity;
import com.employeestracker.exception.AppExceptionHandler;
import com.employeestracker.exception.NotFoundException;
import com.employeestracker.mapper.ProjectMapperImpl;
import com.employeestracker.service.ProjectService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@WebMvcTest(controllers = ProjectController.class)
@AutoConfigureMockMvc(addFilters = false)
@ContextConfiguration(classes = {ProjectController.class, ProjectMapperImpl.class, AppExceptionHandler.class})
class ProjectControllerTest {

    @MockBean
    private ProjectService projectService;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    private ProjectEntity projectEntity;
    private ProjectDto projectDto;

    @BeforeAll
    void setUp() {
        projectDto = new ProjectDto();
        projectDto.setTitle("test");
        projectDto.setStartDate(LocalDate.now());
        projectEntity = new ProjectEntity();
        projectEntity.setId(1L);
        projectEntity.setTitle("test");
    }

    @Test
    void getProject_success_test() throws Exception {
        when(projectService.getProject(eq(1L))).thenReturn(projectEntity);
        mockMvc
                .perform(get("/projects/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(projectEntity.getId()))
                .andExpect(jsonPath("$.title").value(projectEntity.getTitle()));
    }

    @Test
    void getProject_not_found() throws Exception {
        when(projectService.getProject(eq(1L))).thenThrow(NotFoundException.class);
        mockMvc
                .perform(get("/projects/{id}", 1L))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof NotFoundException));
    }

    @Test
    void updateProject_success_test() throws Exception {
        when(projectService.updateProject(eq(projectDto), eq(1L))).thenReturn(projectEntity);
        mockMvc
                .perform(put("/projects/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(projectDto)
                        ))
                .andExpect(status().isOk());
    }

    @Test
    void deleteProject_success_test() throws Exception {
        mockMvc
                .perform(MockMvcRequestBuilders.delete("/projects/{id}", 1L))
                .andExpect(status().isNoContent());
    }
}
