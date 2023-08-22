package com.employeestracker.controller;

import com.employeestracker.dto.ProjectDto;
import com.employeestracker.dto.response.ProjectResponse;
import com.employeestracker.mapper.ProjectMapper;
import com.employeestracker.service.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Positive;

@RestController
@RequestMapping("projects")
@Validated
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService projectService;
    private final ProjectMapper mapper;

    @GetMapping("/{id}")
    public ResponseEntity<ProjectResponse> getProject(@Positive @PathVariable Long id) {
        return ResponseEntity.ok(mapper.toProjectResponse(projectService.getProject(id)));
    }

    @PostMapping
    public ResponseEntity<ProjectResponse> createProject(@Valid @RequestBody ProjectDto userDto) {
        return ResponseEntity.ok(mapper.toProjectResponse(projectService.createProject(userDto)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ProjectResponse> deleteProject(@Positive @PathVariable Long id) {
        projectService.deleteProject(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProjectResponse> updateProject(@Valid @RequestBody ProjectDto projectDto, @Positive @PathVariable Long id) {
        return ResponseEntity.ok(mapper.toProjectResponse(projectService.updateProject(projectDto, id)));
    }
}
