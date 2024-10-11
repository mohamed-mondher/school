package com.school.school.service;


import com.school.school.dto.Classroom;
import com.school.school.repository.ClassroomRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClassroomService {
    private final ClassroomRepository classroomRepository;

    public ClassroomService(ClassroomRepository classroomRepository) {
        this.classroomRepository = classroomRepository;
    }


    public List<Classroom> findAll() {
        return classroomRepository.findAll();
    }


    public Classroom findById(Long id) {
        return classroomRepository.findById(id);
    }

    public Classroom save(Classroom classroom) {
        return classroomRepository.save(classroom);
    }
}
