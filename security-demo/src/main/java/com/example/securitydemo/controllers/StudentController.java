package com.example.securitydemo.controllers;

import com.example.securitydemo.entities.Student;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api/v1/students")
public class StudentController {
    private List<Student> students = new ArrayList<>(
            Arrays.asList(new Student(1L, "furkan"),
                    new Student(2L, "tolga"),
                    new Student(3L, "deneme")));

    @GetMapping("/{studentId}")
    public Student getStudent(@PathVariable Long studentId) {
        return students.stream()
                .filter(student -> student.getId().equals(studentId))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException(studentId + " doesnt exists"));
    }
}
