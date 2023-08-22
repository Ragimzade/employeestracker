package com.employeestracker.service;

import com.employeestracker.dto.UserDto;
import com.employeestracker.entity.UserEntity;
import org.springframework.web.multipart.MultipartFile;

public interface UserService {

    UserEntity createUser(UserDto user);

    void deleteUser(long id);

    UserEntity getUser(long id);

    UserEntity updateUser(UserDto userDto, Long userId);

    void loadUsers(MultipartFile file);
}
