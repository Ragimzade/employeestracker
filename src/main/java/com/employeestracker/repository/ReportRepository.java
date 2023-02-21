package com.employeestracker.repository;

import com.employeestracker.dto.report.ReportType;
import com.employeestracker.entity.ReportEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface ReportRepository extends CrudRepository<ReportEntity, Long> {

    Optional<ReportEntity> findTop1ByTypeOrderByCreatedDesc(ReportType reportType);

    Optional<ReportEntity> findTop1ByTypeAndNameOrderByCreatedDesc(ReportType reportType, String reportName);

}
