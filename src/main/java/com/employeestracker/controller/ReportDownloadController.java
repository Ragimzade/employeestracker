package com.employeestracker.controller;

import com.employeestracker.entity.ReportEntity;
import com.employeestracker.service.impl.ReportServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;

@RestController
@RequestMapping("report")
@RequiredArgsConstructor
public class ReportDownloadController {

    private final ReportServiceImpl reportGeneratorService;

    @GetMapping("/download/availability")
    public ResponseEntity<byte[]> downloadUsersAvailabilityReport() {
        ReportEntity file = reportGeneratorService.getLatestAvailability();
        return new ResponseEntity<>(file.getData(), buildHeaders(file), HttpStatus.OK);
    }

    @GetMapping("/download/workload/{departmentTitle}")
    public ResponseEntity<byte[]> downloadUsersWorkloadReport(@NotNull @PathVariable String departmentTitle) {
        ReportEntity file = reportGeneratorService.getLatestWorkloadByDepartment(departmentTitle);
        return new ResponseEntity<>(file.getData(), buildHeaders(file), HttpStatus.OK);
    }

    private HttpHeaders buildHeaders(ReportEntity file) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("application/vnd.ms-excel"));
        headers.setContentLength(file.getData().length);
        headers.setContentDispositionFormData("attachment", file.getName() + file.getCreated() + ".xlsx");
        return headers;
    }
}
