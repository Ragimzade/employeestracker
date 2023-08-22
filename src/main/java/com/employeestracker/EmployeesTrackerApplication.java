package com.employeestracker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.validation.annotation.Validated;

@Validated
@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
public class EmployeesTrackerApplication {

    public static void main(String[] args) {
        SpringApplication.run(EmployeesTrackerApplication.class, args);
    }
}
