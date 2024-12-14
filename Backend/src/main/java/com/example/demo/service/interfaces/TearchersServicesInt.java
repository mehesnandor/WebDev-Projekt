package com.example.demo.service.interfaces;

import com.example.demo.model.Courses;
import com.example.demo.model.Users;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface TearchersServicesInt {

    //Create
    ResponseEntity<Courses> saveCourse(Courses course);

    //Read
    ResponseEntity<Courses> detailsOfACourse(Long courseId);
    ResponseEntity<List<Users>> appointmentsForThisCourse(Long courseId);
    ResponseEntity<List<Courses>> listOfMyCourses(Long teacherId);

    //Update
    ResponseEntity<?> updateThisCourse(Courses course);

    //Delete
    ResponseEntity<?> deleteCourse(Long courseId);
    ResponseEntity<?> deleteAppointment(Long courseId, Long studentId);

}
