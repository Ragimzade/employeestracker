package com.employeestracker.controller;

import com.employeestracker.dto.DepartmentDto;
import com.employeestracker.entity.DepartmentEntity;
import com.employeestracker.exception.AppExceptionHandler;
import com.employeestracker.exception.NotFoundException;
import com.employeestracker.mapper.DepartmentMapperImpl;
import com.employeestracker.service.DepartmentService;
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

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@WebMvcTest(controllers = DepartmentController.class)
@AutoConfigureMockMvc(addFilters = false)
@ContextConfiguration(classes = {DepartmentController.class, DepartmentMapperImpl.class, AppExceptionHandler.class})
class DepartmentControllerTest {

    @MockBean
    private DepartmentService departmentService;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    private DepartmentEntity departmentEntity;
    private DepartmentDto departmentDto;

    @BeforeAll
    void setUp() {
        departmentDto = new DepartmentDto();
        departmentDto.setTitle("test");
        departmentEntity = new DepartmentEntity();
        departmentEntity.setId(1L);
        departmentEntity.setTitle("test");
    }

    @Test
    void getDepartment_success_test() throws Exception {
        when(departmentService.getDepartment(eq(1L))).thenReturn(departmentEntity);
        mockMvc
                .perform(get("/departments/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(departmentEntity.getId()))
                .andExpect(jsonPath("$.title").value(departmentEntity.getTitle()));
    }

    @Test
    void getDepartment_not_found() throws Exception {
        when(departmentService.getDepartment(eq(1L))).thenThrow(NotFoundException.class);
        mockMvc
                .perform(get("/departments/{id}", 1L))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof NotFoundException));
    }

    @Test
    void updateDepartment_success_test() throws Exception {
        when(departmentService.updateDepartment(eq(departmentDto), eq(1L))).thenReturn(departmentEntity);
        mockMvc
                .perform(put("/departments/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(departmentDto)
                        ))
                .andExpect(status().isOk());
    }

    @Test
    void deleteDepartment_success_test() throws Exception {
        mockMvc
                .perform(MockMvcRequestBuilders.delete("/departments/{id}", 1L))
                .andExpect(status().isNoContent());
    }
}
