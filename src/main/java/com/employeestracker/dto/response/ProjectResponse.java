package com.employeestracker.dto.response;

import lombok.Data;

import java.time.LocalDate;

@Data
public class ProjectResponse {

    private String id;
    private String title;
    private LocalDate startDate;
    private LocalDate endDate;
}
