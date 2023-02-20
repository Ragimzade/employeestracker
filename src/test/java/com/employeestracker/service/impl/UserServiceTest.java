package com.employeestracker.service.impl;

import com.employeestracker.dto.UserDto;
import com.employeestracker.entity.DepartmentEntity;
import com.employeestracker.entity.UserEntity;
import com.employeestracker.exception.AlreadyExistsException;
import com.employeestracker.exception.NotFoundException;
import com.employeestracker.mapper.UserMapper;
import com.employeestracker.mapper.UserMapperImpl;
import com.employeestracker.repository.DepartmentRepository;
import com.employeestracker.repository.UserRepository;
import com.employeestracker.service.UserService;
import com.employeestracker.util.CSVReader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.util.ResourceUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@ContextConfiguration(classes = {UserServiceImpl.class, UserMapperImpl.class, CSVReader.class})
class UserServiceTest {

    @Autowired
    private UserService userService;
    @Autowired
    private CSVReader<UserDto> csvReader;
    @MockBean
    private UserRepository userRepository;
    @MockBean
    private DepartmentRepository departmentRepository;
    @MockBean
    private UserMapper userMapper;

    private UserDto userDto;
    private UserDto userDto2;
    private UserEntity userEntity;
    private DepartmentEntity departmentEntity;

    @BeforeEach
    void setup() {
        userDto = new UserDto();
        userDto.setEmail("test@example.com");
        userDto.setDepartmentTitle("Test Department");
        userDto2 = new UserDto();
        userDto2.setEmail("test2@example.com");
        userDto2.setDepartmentTitle("Test Department");
        userEntity = new UserEntity();
        departmentEntity = new DepartmentEntity();
    }

    @Test()
    void createUser_user_already_exists() {
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(new UserEntity()));
        assertThrows(AlreadyExistsException.class, () -> userService.createUser(userDto));
    }

    @Test
    void createUser_success_test() {
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.empty());
        when(departmentRepository.findDepartmentEntityByTitle("Test Department")).thenReturn(Optional.of(departmentEntity));
        when(userMapper.toUserEntity(userDto, departmentEntity)).thenReturn(userEntity);
        when(userRepository.save(userEntity)).thenReturn(userEntity);
        UserEntity result = userService.createUser(userDto);

        verify(userRepository, times(1)).save(any());
        assertEquals(userEntity, result);
    }

    @Test
    void getUser_user_found() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(userEntity));
        UserEntity result = userService.getUser(1L);
        assertEquals(userEntity, result);
    }

    @Test
    void getUser_user_not_found() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> userService.getUser(1L));
    }

    @Test
    void loadUsers_success() throws IOException {
        File file = ResourceUtils.getFile("classpath:csv/users.csv");
        FileInputStream inputStream = new FileInputStream(file);
        when(departmentRepository.findDepartmentEntityByTitle(any())).thenReturn(Optional.ofNullable(departmentEntity));
        MultipartFile multipartFile = new MockMultipartFile(file.getName(),
                file.getName(), "application/octet-stream", inputStream);

        List<UserDto> usersList = csvReader.read(multipartFile, UserDto.class);

        userService.loadUsers(multipartFile);

        verify(userRepository, times(usersList.size())).save(any());
    }
}

