package com.employeestracker.dto.report;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserWorkload {

    private String fullName;
    private String department;
    private String project;
    private String occupation;
}
