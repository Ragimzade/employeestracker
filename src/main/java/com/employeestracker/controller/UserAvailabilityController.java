package com.employeestracker.controller;

import com.employeestracker.dto.response.AvailableUsersResponse;
import com.employeestracker.entity.UserEntity;
import com.employeestracker.mapper.AvailableUserResponseMapper;
import com.employeestracker.service.UsersAvailabilityService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.Positive;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("available")
@RequiredArgsConstructor
public class UserAvailabilityController {

    private final UsersAvailabilityService usersAvailabilityService;
    private final AvailableUserResponseMapper mapper;

    @GetMapping("/{period}")
    public List<AvailableUsersResponse> findAllAvailableEmployeeInPeriod(@Positive @PathVariable("period") Integer period) {

        LocalDate range = LocalDate.now().plusDays(period);
        List<UserEntity> allAvailableUsers = usersAvailabilityService.getAvailableUsers(range);
        return allAvailableUsers.stream()
                .map(user -> {
                    LocalDate fromDate = usersAvailabilityService.getUserAvailabilityFromDate(user, range);
                    LocalDate toDate = usersAvailabilityService.getUserAvailabilityToDate(user, range);
                    return mapper.toAvailableUsersResponse(user, fromDate, toDate);
                })
                .toList();
    }
}


