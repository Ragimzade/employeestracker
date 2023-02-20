package com.employeestracker.controller;

import com.employeestracker.dto.UserDto;
import com.employeestracker.entity.DepartmentEntity;
import com.employeestracker.entity.UserEntity;
import com.employeestracker.exception.AppExceptionHandler;
import com.employeestracker.exception.NotFoundException;
import com.employeestracker.mapper.UserMapperImpl;
import com.employeestracker.repository.UserRepository;
import com.employeestracker.service.UserService;
import com.employeestracker.util.CSVReader;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = UserController.class)
@AutoConfigureMockMvc(addFilters = false)
@ContextConfiguration(classes = {UserController.class, UserMapperImpl.class, AppExceptionHandler.class})
class UserServiceControllerTest {

    @MockBean
    private UserService userService;
    @MockBean
    private UserRepository userRepository;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private CSVReader<UserDto> csvReader;
    private static UserEntity userEntity;
    private static UserDto userDto;


    @BeforeAll
    static void doBefore() {
        userEntity = new UserEntity();
        DepartmentEntity departmentEntity = new DepartmentEntity();
        departmentEntity.setTitle("java");
        userEntity.setId(1L);
        userEntity.setEmail("test@mail.com");
        userEntity.setFirstName("testName");
        userEntity.setLastName("testLastName");
        userEntity.setJobTitle("developer");
        userEntity.setDepartment(departmentEntity);
        userDto = new UserDto();
        userDto.setFirstName("name");
        userDto.setEmail("test2@gmail.com");
    }

    @Test
    void getUser_success_test() throws Exception {
        when(userService.getUser(eq(1L))).thenReturn(userEntity);
        mockMvc
                .perform(get("/users/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(userEntity.getId()))
                .andExpect(jsonPath("$.firstName").value(userEntity.getFirstName()))
                .andExpect(jsonPath("$.email").value(userEntity.getEmail()))
                .andExpect(jsonPath("$.jobTitle").value(userEntity.getJobTitle()))
                .andExpect(jsonPath("$.department").value(userEntity.getDepartment().getTitle()));
    }

    @Test
    void getUser_not_found() throws Exception {
        Long userId = 1L;
        when(userService.getUser(eq(userId))).thenThrow(NotFoundException.class);
        mockMvc
                .perform(get("/users/{id}", userId))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof NotFoundException));
    }

    @Test
    void updateUser_success_test() throws Exception {
        Long userId = 1L;
        when(userService.updateUser(eq(userDto), eq(userId))).thenReturn(userEntity);
        mockMvc
                .perform(put("/users/{id}", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDto)
                        ))
                .andExpect(status().isOk());
    }

    @Test
    void uploadUsers_success_test() throws Exception {
        String csvData = "username,lastname,email, department;";
        MockMultipartFile csvFile = new MockMultipartFile("file", "users.csv", "text/csv", csvData.getBytes());
        mockMvc.perform(MockMvcRequestBuilders.multipart("/users/upload")
                        .file(csvFile))
                .andExpect(status().isOk());
        verify(userService, times(1)).loadUsers(csvFile);
    }

    @Test
    void deleteUser_success_test() throws Exception {
        long userId = 1L;
        mockMvc
                .perform(MockMvcRequestBuilders.delete("/users/{id}", userId))
                .andExpect(status().isNoContent());
        verify(userService, times(1)).deleteUser(userId);

    }
}
