package com.example.demo.service.interfaces;

import com.example.demo.model.Courses;
import com.example.demo.model.Users;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface AdminsServicesInt {

    //Create
    ResponseEntity<?> createUser(Users user);
    ResponseEntity<?> createCourse(Courses course);

    //Read
    ResponseEntity<List<Users>> getAllUsers();
    ResponseEntity<List<Courses>> getAllCourses();
    ResponseEntity<Courses> getCourseById(Long id);
    ResponseEntity<Users> getUserById(Long id);
    ResponseEntity<List<Users>> appointmentsForThisCourse(Long courseId);

    //Update
    ResponseEntity<?> updateUser(Users user);
    ResponseEntity<?> updateCourse(Courses course);

    //Delete
    ResponseEntity<?> deleteUser(Long id);
    ResponseEntity<?> deleteCourse(Long id);

}
