package com.employeestracker.service.impl;

import com.employeestracker.entity.ProjectPositionEntity;
import com.employeestracker.entity.UserEntity;
import com.employeestracker.repository.UserRepository;
import com.employeestracker.service.UsersAvailabilityService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@SpringBootTest
@ContextConfiguration(classes = {UsersAvailabilityServiceImpl.class})
class UsersAvailabilityServiceTest {

    @Autowired
    private UsersAvailabilityService availabilityService;
    @MockBean
    private UserRepository repository;
    private ProjectPositionEntity projectPosition;
    private ProjectPositionEntity projectPosition2;
    private UserEntity user;

    @BeforeEach
    void setUp() {
        projectPosition = new ProjectPositionEntity();
        projectPosition.setId(1L);
        projectPosition2 = new ProjectPositionEntity();
        projectPosition2.setId(2L);

        user = new UserEntity();
    }

    @Test
    void getUserAvailabilityFromDate_successTest() {
        projectPosition.setStartDate(LocalDate.now().minusDays(15));
        projectPosition.setEndDate(LocalDate.now().plusDays(5));

        user.setProjectPositions(List.of(projectPosition));

        LocalDate actualFromDate = availabilityService.getUserAvailabilityFromDate(user, LocalDate.now().plusDays(10));
        assertEquals(LocalDate.now().plusDays(5), actualFromDate);
    }

    @Test
    void getUserAvailabilityFromDate_noProjectPosition() {
        LocalDate actualFromDate = availabilityService.getUserAvailabilityFromDate(user, LocalDate.now().plusDays(10));
        assertEquals(LocalDate.now(), actualFromDate);
    }

    @Test
    void getUserAvailabilityFromDate_projectPositionEndedInThePast() {
        projectPosition.setEndDate(LocalDate.now().minusDays(10));
        user.setProjectPositions(List.of(projectPosition));

        LocalDate actualFromDate = availabilityService.getUserAvailabilityFromDate(user, LocalDate.now().plusDays(10));
        assertEquals(LocalDate.now(), actualFromDate);
    }

    @Test
    void getUserAvailabilityToDate_successTest() {
        projectPosition.setId(1L);
        projectPosition.setStartDate(LocalDate.now().minusDays(15));
        projectPosition.setEndDate(LocalDate.now().plusDays(5));

        projectPosition2.setId(2L);
        projectPosition2.setStartDate(LocalDate.now().plusDays(30));

        user.setProjectPositions(List.of(projectPosition, projectPosition2));

        LocalDate actualToDate = availabilityService.getUserAvailabilityToDate(user, LocalDate.now().plusDays(10));
        assertEquals(LocalDate.now().plusDays(30), actualToDate);
    }

    @Test
    void getUserAvailabilityToDate_nullEndDate() {
        projectPosition.setStartDate(LocalDate.now().minusDays(15));
        projectPosition.setEndDate(null);
        user.setProjectPositions(List.of(projectPosition));

        LocalDate actualToDate = availabilityService.getUserAvailabilityToDate(user, LocalDate.now().plusDays(10));
        assertNull(actualToDate);
    }

    @Test
    void getUserAvailabilityToDate_endDateInThePast() {
        projectPosition.setStartDate(LocalDate.now().minusDays(15));
        projectPosition.setEndDate(null);
        user.setProjectPositions(List.of(projectPosition));

        LocalDate actualToDate = availabilityService.getUserAvailabilityToDate(user, LocalDate.now().plusDays(10));
        assertNull(actualToDate);
    }
}
