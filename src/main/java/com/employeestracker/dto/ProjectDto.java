package com.employeestracker.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
public class ProjectDto {

    @NotNull(message = "title is mandatory")
    private String title;

    @NotNull(message = "startDate is mandatory")
    private LocalDate startDate;

    private LocalDate endDate;

}
