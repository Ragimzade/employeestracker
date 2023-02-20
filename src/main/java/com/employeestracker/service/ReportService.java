package com.employeestracker.service;

import com.employeestracker.entity.ReportEntity;

public interface ReportService {

    ReportEntity getLatestWorkloadByDepartment(String departmentTitle);

    ReportEntity getLatestAvailability();

    void generateAvailabilityReport();

    void generateDepartmentWorkloadReports();
}
