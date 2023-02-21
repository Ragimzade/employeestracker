package com.employeestracker.service.impl;

import com.employeestracker.dto.report.ReportType;
import com.employeestracker.dto.report.UserWorkload;
import com.employeestracker.dto.report.UsersAvailability;
import com.employeestracker.entity.DepartmentEntity;
import com.employeestracker.entity.ProjectPositionEntity;
import com.employeestracker.entity.ReportEntity;
import com.employeestracker.entity.UserEntity;
import com.employeestracker.exception.NotFoundException;
import com.employeestracker.repository.ReportRepository;
import com.employeestracker.repository.UserRepository;
import com.employeestracker.service.DepartmentService;
import com.employeestracker.service.ProjectPositionService;
import com.employeestracker.service.ReportService;
import com.employeestracker.service.UsersAvailabilityService;
import com.employeestracker.util.ExcelGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.employeestracker.dto.report.ReportType.AVAILABILITY;
import static com.employeestracker.dto.report.ReportType.WORKLOAD;
import static java.time.LocalDate.now;
import static java.util.stream.Collectors.toMap;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReportServiceImpl implements ReportService {

    private static final String AVAILABILITY_REPORT_NAME = "Availability";
    private static final String NO_ACTIVE_PROJECT_NAME = "No active project";

    private final DepartmentService departmentService;
    private final ProjectPositionService projectPositionService;
    private final ReportRepository repository;
    private final UserRepository userRepository;
    private final UsersAvailabilityService usersAvailabilityService;
    private final ExcelGenerator<UserWorkload> workloadExcelGenerator;
    private final ExcelGenerator<UsersAvailability> availabilityExcelGenerator;

    @EventListener(ApplicationStartedEvent.class)
    public void doSmt() {
        generateDepartmentWorkloadReports();
        generateAvailabilityReport();
    }

    @Override
    public ReportEntity getLatestWorkloadByDepartment(String departmentTitle) {
        return repository.findTop1ByTypeAndNameOrderByCreatedDesc(WORKLOAD, departmentTitle)
                .orElseThrow(() -> new NotFoundException("Workload report for department '%s' not found"
                        .formatted(departmentTitle)));
    }

    @Override
    public ReportEntity getLatestAvailability() {
        return repository.findTop1ByTypeOrderByCreatedDesc(AVAILABILITY)
                .orElseThrow(() -> new NotFoundException("No availability report found"));
    }

    @Override
    public void generateDepartmentWorkloadReports() {
        log.debug("Started generation of workload reports for departments");
        departmentService.getAllDepartments()
                .stream()
                .collect(toMap(d -> d, userRepository::findAllByDepartment))
                .forEach(this::generateWorkloadReport);
    }

    @Override
    public void generateAvailabilityReport() {
        log.debug("Started generation of user availability report");
        List<UsersAvailability> availableUsers = usersAvailabilityService.getAvailableUsers(now().plusMonths(1))
                .stream()
                .map(this::builUsersAvailability)
                .toList();

        byte[] report = availabilityExcelGenerator.generate(availableUsers, UsersAvailability.class);
        ReportEntity reportEntity = ReportEntity.builder()
                .name(AVAILABILITY_REPORT_NAME)
                .type(ReportType.AVAILABILITY)
                .data(report)
                .build();
        repository.save(reportEntity);
    }

    private void generateWorkloadReport(DepartmentEntity department, List<UserEntity> users) {
        List<UserWorkload> userWorkloads = getWorkloadsList(users);
        byte[] report = workloadExcelGenerator.generate(userWorkloads, UserWorkload.class);
        ReportEntity reportEntity = ReportEntity.builder()
                .name(department.getTitle())
                .type(WORKLOAD)
                .data(report)
                .build();
        repository.save(reportEntity);
    }

    private List<UserWorkload> getWorkloadsList(List<UserEntity> users) {
        return users.stream()
                .map(user -> {
                    ProjectPositionEntity project = projectPositionService.getUserActiveProjectPosition(user.getId());
                    return buildUserWorkload(user, project);
                })
                .collect(Collectors.toList());
    }

    private UserWorkload buildUserWorkload(UserEntity user, ProjectPositionEntity projectPosition) {
        String project = Optional.ofNullable(projectPosition)
                .map(pp -> pp.getProjects().getTitle())
                .orElse(NO_ACTIVE_PROJECT_NAME);

        String occupation = Optional.ofNullable(projectPosition)
                .map(ProjectPositionEntity::getOccupation)
                .orElse(null);

        return UserWorkload.builder()
                .fullName(user.getFullName())
                .department(user.getDepartment().getTitle())
                .project(project)
                .occupation(occupation)
                .build();

    }

    private UsersAvailability builUsersAvailability(UserEntity user) {
        ProjectPositionEntity entity = projectPositionService.getUserActiveProjectPosition(user.getId());

        LocalDate projectEndDate = entity == null ? null : entity.getEndDate();
        String project = entity == null ? NO_ACTIVE_PROJECT_NAME : entity.getProjects().getTitle();

        return UsersAvailability.builder()
                .fullName(user.getFullName())
                .department(user.getDepartment() == null ? null : user.getDepartment().getTitle())
                .project(project)
                .projectPositionEndDate(projectEndDate)
                .build();
    }
}
