package com.employeestracker.service.impl;

import com.employeestracker.entity.ProjectPositionEntity;
import com.employeestracker.entity.UserEntity;
import com.employeestracker.repository.UserRepository;
import com.employeestracker.service.UsersAvailabilityService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UsersAvailabilityServiceImpl implements UsersAvailabilityService {

    private final UserRepository userRepository;

    @Override
    public List<UserEntity> getAvailableUsers(LocalDate range) {
        return userRepository.findAvailableUsers(LocalDate.now(), range);
    }

    @Override
    public LocalDate getUserAvailabilityFromDate(UserEntity user, LocalDate range) {
        List<ProjectPositionEntity> projectPositions = user.getProjectPositions();
        if (projectPositions == null) {
            return LocalDate.now();
        }

        LocalDate latestEndDate = projectPositions.stream()
                .map(ProjectPositionEntity::getEndDate)
                .filter(p -> p.isBefore(range.plusDays(1)))
                .max(LocalDate::compareTo)
                .orElse(LocalDate.now().minusDays(1));

        return latestEndDate.isBefore(LocalDate.now()) ? LocalDate.now() : latestEndDate;

    }

    @Override
    public LocalDate getUserAvailabilityToDate(UserEntity user, LocalDate range) {
        List<ProjectPositionEntity> projectPositions = user.getProjectPositions();
        return projectPositions.stream()
                .map(ProjectPositionEntity::getStartDate)
                .max(LocalDate::compareTo)
                .filter(p -> p.isAfter(range))
                .orElse(null);
    }
}
