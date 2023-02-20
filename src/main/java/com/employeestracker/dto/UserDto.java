package com.employeestracker.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class UserDto {

    @NotNull(message = "firstName is mandatory")
    private String firstName;

    @NotNull(message = "lastName is mandatory")
    private String lastName;

    @Email(regexp = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$", message = "Please, provide a valid email")
    private String email;

    @NotNull(message = "jobTitle is mandatory")
    private String jobTitle;

    @NotNull(message = "departmentId is mandatory")
    private String departmentTitle;

    private String password;
}
