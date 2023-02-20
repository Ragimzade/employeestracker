package com.employeestracker.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
public class ProjectPositionDto {

    @NotNull(message = "user id is mandatory")
    private Long userId;

    @NotNull(message = "project id is mandatory")
    private Long projectId;

    @NotNull(message = "occupation is mandatory")
    private String occupation;

    @NotNull(message = "position title is mandatory")
    private String positionTitle;

    @NotNull(message = "start date is mandatory")
    private LocalDate startDate;

    private LocalDate endDate;
}
