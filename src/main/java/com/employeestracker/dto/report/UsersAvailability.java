package com.employeestracker.dto.report;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class UsersAvailability {

    private String fullName;
    private String department;
    private String project;
    private LocalDate projectPositionEndDate;
}
