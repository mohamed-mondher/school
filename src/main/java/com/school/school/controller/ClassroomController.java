package com.school.school.controller;


import com.school.school.dto.Classroom;
import com.school.school.service.ClassroomService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ClassroomController {

    private final ClassroomService classroomService;


    public ClassroomController(ClassroomService classroomService) {
        this.classroomService = classroomService;
    }

    @PreAuthorize("hasAnyAuthority('Admin')")
    @PostMapping("/v1/classrooms")
    public ResponseEntity<Classroom> createStudent(@RequestBody Classroom classroom) {
        Classroom savedClassroom = classroomService.save(classroom);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedClassroom);
    }


    @PreAuthorize("hasAnyAuthority('Admin')")
    @GetMapping("/v1/classrooms")
    public ResponseEntity<List<Classroom>> findAll() {
        List<Classroom> classrooms = classroomService.findAll();
        return ResponseEntity.status(HttpStatus.OK).body(classrooms);
    }

    @PreAuthorize("hasAnyAuthority('Admin')")
    @GetMapping("/v1/classrooms/{id}")
    public ResponseEntity<Classroom> findById(@PathVariable Long id) {
        Classroom classroom = classroomService.findById(id);
        return ResponseEntity.status(HttpStatus.OK).body(classroom);
    }
}
