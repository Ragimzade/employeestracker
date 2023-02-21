package com.employeestracker.controller;

import com.employeestracker.dto.DepartmentDto;
import com.employeestracker.dto.response.DepartmentResponse;
import com.employeestracker.mapper.DepartmentMapper;
import com.employeestracker.service.DepartmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Positive;

@RestController
@RequestMapping("departments")
@Validated
@RequiredArgsConstructor
public class DepartmentController {

    private final DepartmentService departmentService;
    private final DepartmentMapper mapper;

    @GetMapping("/{id}")
    public ResponseEntity<DepartmentResponse> getDepartment(@Positive @PathVariable Long id) {
        return ResponseEntity.ok(mapper.toDepartmentResponse(departmentService.getDepartment(id)));
    }

    @PostMapping
    public ResponseEntity<DepartmentResponse> createDepartment(@Valid @RequestBody DepartmentDto dto) {
        return ResponseEntity.ok(mapper.toDepartmentResponse(departmentService.createDepartment(dto)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<DepartmentResponse> deleteDepartment(@Positive @PathVariable Long id) {
        departmentService.deleteDepartment(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DepartmentResponse> updateDepartment(@Valid @RequestBody DepartmentDto dto, @Positive @PathVariable Long id) {
        return ResponseEntity.ok(mapper.toDepartmentResponse(departmentService.updateDepartment(dto, id)));
    }

}
