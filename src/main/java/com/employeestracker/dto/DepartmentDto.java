package com.employeestracker.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;


@Data
public class DepartmentDto {

    @NotNull(message = "Title is mandatory")
    private String title;
}
