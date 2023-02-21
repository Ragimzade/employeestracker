package com.employeestracker.dto.response;

import lombok.Data;

import java.time.LocalDate;

@Data
public class ProjectPositionResponse {

    private Long id;
    private Long userId;
    private Long projectId;
    private String occupation;
    private String positionTitle;
    private LocalDate startDate;
    private LocalDate endDate;
}
