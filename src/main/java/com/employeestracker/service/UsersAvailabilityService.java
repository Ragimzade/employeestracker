package com.employeestracker.service;

import com.employeestracker.entity.UserEntity;

import java.time.LocalDate;
import java.util.List;

public interface UsersAvailabilityService {

    LocalDate getUserAvailabilityFromDate(UserEntity user, LocalDate range);

    LocalDate getUserAvailabilityToDate(UserEntity user, LocalDate range);

    List<UserEntity> getAvailableUsers(LocalDate range);

}
