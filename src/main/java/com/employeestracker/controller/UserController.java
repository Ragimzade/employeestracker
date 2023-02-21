package com.employeestracker.controller;

import com.employeestracker.dto.UserDto;
import com.employeestracker.dto.response.UserResponse;
import com.employeestracker.mapper.UserMapper;
import com.employeestracker.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import javax.validation.constraints.Positive;

@RestController
@RequestMapping("users")
@Validated
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<UserResponse> getUser(@Positive @PathVariable Long id) {
        return ResponseEntity.ok(userMapper.toUserResponse(userService.getUser(id)));
    }

    @PostMapping
    public ResponseEntity<UserResponse> createUser(@Valid @RequestBody UserDto userDto) {
        return ResponseEntity.ok(userMapper.toUserResponse(userService.createUser(userDto)));
    }

    @PostMapping("/upload")
    public ResponseEntity<Void> uploadUsersFromSCV(@RequestParam("file") MultipartFile file) {
        userService.loadUsers(file);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<UserResponse> deleteUser(@Positive @PathVariable Long id) {
        userService.deleteUser(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponse> updateUser(@Valid @RequestBody UserDto userDto, @Positive @PathVariable Long id) {
        return ResponseEntity.ok(userMapper.toUserResponse(userService.updateUser(userDto, id)));
    }
}
