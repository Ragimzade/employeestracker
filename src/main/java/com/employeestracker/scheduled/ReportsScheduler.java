package com.employeestracker.scheduled;

import com.employeestracker.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ReportsScheduler {

    private final ReportService reportService;

    @Scheduled(cron = "${scheduled.task.cron.expression}")
    public void generateReports() {
        reportService.generateAvailabilityReport();
        reportService.generateDepartmentWorkloadReports();
    }
}
