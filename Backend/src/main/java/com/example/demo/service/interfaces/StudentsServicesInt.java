package com.example.demo.service.interfaces;

import com.example.demo.model.Courses;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface StudentsServicesInt {

    //Create
    ResponseEntity<?> makeAnAppointment(Long sid, Long courseId);

    //Read
    ResponseEntity<List<Courses>> listOfMyCourses(Long studentId);
    ResponseEntity<List<Courses>> listOfNotMyCourses(String username);

    //Delete
    ResponseEntity<?> deleteAppointment(Long sid, Long courseId);

}
