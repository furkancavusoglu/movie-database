package com.example.securitydemo.controllers;

import com.example.securitydemo.entities.Student;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import static com.example.securitydemo.security.Roles.ADMIN;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/v1/management")
public class AdminController {
    private List<Student> students = new ArrayList<>(
            Arrays.asList(new Student(1L, "furkan"),
                    new Student(2L, "tolga"),
                    new Student(3L, "deneme")));

    @PutMapping("/{studentId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public Student putStudent(@PathVariable Long studentId, @RequestBody String name) {
        return students.stream()
                .filter(student -> student.getId().equals(studentId))
                .findFirst()
                .orElse(new Student(studentId, name));
    }
}
