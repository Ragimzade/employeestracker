package com.employeestracker.service.impl;

import com.employeestracker.dto.UserDto;
import com.employeestracker.entity.DepartmentEntity;
import com.employeestracker.entity.UserEntity;
import com.employeestracker.exception.AlreadyExistsException;
import com.employeestracker.exception.NotFoundException;
import com.employeestracker.mapper.UserMapper;
import com.employeestracker.repository.DepartmentRepository;
import com.employeestracker.repository.UserRepository;
import com.employeestracker.service.UserService;
import com.employeestracker.util.CSVReader;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final DepartmentRepository departmentRepository;
    private final UserMapper userMapper;
    private final CSVReader<UserDto> csvReader;


    @Override
    public UserEntity createUser(UserDto user) {
        userRepository.findByEmail(user.getEmail()).ifPresent(u -> {
            throw new AlreadyExistsException("User with email '%s' already exists".formatted(user.getEmail()));
        });

        String department = user.getDepartmentTitle();

        DepartmentEntity departmentEntity = departmentRepository
                .findDepartmentEntityByTitle(department)
                .orElseThrow(() -> new NotFoundException("Department '%s' not found".formatted(department)));
        var entity = userMapper.toUserEntity(user, departmentEntity);
        return userRepository.save(entity);
    }

    @Transactional
    @Override
    public void loadUsers(MultipartFile file) {
        List<UserDto> read = csvReader.read(file, UserDto.class);
        read.forEach(this::createUser);
    }

    @Override
    public UserEntity getUser(long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User with id '%s' is not found".formatted(id)));
    }

    @Override
    public UserEntity updateUser(UserDto userDto, Long id) {
        UserEntity userEntity = getUser(id);

        DepartmentEntity departmentEntity = departmentRepository
                .findDepartmentEntityByTitle(userDto.getDepartmentTitle())
                .orElseThrow(() -> new NotFoundException("Department '%s' is not found".formatted(userDto.getDepartmentTitle())));

        userMapper.updateUserEntityFromDto(userDto, userEntity, departmentEntity);
        return userRepository.save(userEntity);
    }

    @Override
    public void deleteUser(long id) {
        UserEntity userEntity = getUser(id);
        userRepository.delete(userEntity);
    }
}
