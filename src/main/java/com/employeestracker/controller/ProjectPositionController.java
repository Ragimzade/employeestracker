package com.employeestracker.controller;

import com.employeestracker.dto.ProjectPositionDto;
import com.employeestracker.dto.response.ProjectPositionResponse;
import com.employeestracker.mapper.ProjectPositionMapper;
import com.employeestracker.service.ProjectPositionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Positive;

@RestController
@RequestMapping("project-positions")
@Validated
@RequiredArgsConstructor
public class ProjectPositionController {

    private final ProjectPositionService projectPositionService;
    private final ProjectPositionMapper mapper;

    @GetMapping("/{id}")
    public ResponseEntity<ProjectPositionResponse> getProjectPosition(@Positive @PathVariable Long id) {
        return ResponseEntity.ok(mapper.toProjectPositionResponse(projectPositionService.getProjectPosition(id)));
    }

    @PostMapping
    public ResponseEntity<ProjectPositionResponse> createProjectPosition(@Valid @RequestBody ProjectPositionDto dto) {
        return ResponseEntity.ok(mapper.toProjectPositionResponse(projectPositionService.createProjectPosition(dto)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ProjectPositionResponse> deleteProjectPosition(@Positive @PathVariable Long id) {
        projectPositionService.deleteProjectPosition(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProjectPositionResponse> updateProjectPosition(@Valid @RequestBody ProjectPositionDto dto, @Positive @PathVariable Long id) {
        return ResponseEntity.ok(mapper.toProjectPositionResponse(projectPositionService.updateProjectPosition(dto, id)));
    }
}
