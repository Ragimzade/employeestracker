package com.employeestracker.dto.response;

import lombok.Data;

import java.time.LocalDate;

@Data
public class AvailableUsersResponse {
    private String id;
    private String firstName;
    private String lastName;
    private String email;
    private String jobTitle;
    private String department;
    private LocalDate availableFrom;
    private LocalDate availableTo;

}
