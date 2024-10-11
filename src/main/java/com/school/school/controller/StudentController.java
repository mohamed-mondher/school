package com.school.school.controller;

import com.school.school.dto.Student;
import com.school.school.service.StudentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @PreAuthorize("hasAnyAuthority('Admin')")
    @GetMapping("/v1/students")
    public ResponseEntity<List<Student>> getAllStudents() {

        return ResponseEntity.status(HttpStatus.OK).body(studentService.findAll());
    }

    @PreAuthorize("hasAnyAuthority('Admin')")
    @PostMapping("/v1/students")
    public ResponseEntity<Student> createStudent(@RequestBody Student student) {

        Student savedStudent = studentService.save(student);

        return ResponseEntity.status(HttpStatus.CREATED).body(savedStudent);
    }

    @PreAuthorize("hasAnyAuthority('Admin')")
    @PutMapping("/v1/students")
    public ResponseEntity<Student> createStudent(@RequestParam Long id, Long classroomId) {

        Student savedStudent = studentService.assignToClassroom(id, classroomId);

        return ResponseEntity.status(HttpStatus.CREATED).body(savedStudent);
    }

    @PreAuthorize("hasAnyAuthority('Admin')")
    @GetMapping("/v1/students/{id}")
    public ResponseEntity<Student> getUserById(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(studentService.findById(id));
    }
}
