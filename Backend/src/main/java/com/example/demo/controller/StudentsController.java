package com.example.demo.controller;

import com.example.demo.model.Courses;
import com.example.demo.service.StudentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path = "api/v1/student")
@RequiredArgsConstructor
@Slf4j
public class StudentsController{

    @Autowired
    private final StudentService studentService;

    /**A Student csak a kurzusokkal tudnak interaktálni.*/

    /**Tudnak jelentkezni kurzusokra*/
    @PostMapping(path = "/create/{sid}/{cid}")
    public ResponseEntity<?> makeAnAppointment(@PathVariable("sid") Long sid, @PathVariable("cid") Long courseId){
        log.info("[Student-Controller] MakeAnAppointment for course with ID " + courseId + " and student " + sid);
        return studentService.makeAnAppointment(sid, courseId);
    }

    /**Listázni a kurzusaikat*/
    @GetMapping(path = "/mycourses/{sid}")
    public ResponseEntity<List<Courses>> listOfMyCourses(@PathVariable("sid") Long studentId){
        log.info("[Student-Controller] Listing the my courses");
        return studentService.listOfMyCourses(studentId);
    }

    /**Listázni a további kurzusokat, amikre még nincsen jelentkezése*/
    @GetMapping(path = "/notmycourses/{username}")
    public ResponseEntity<List<Courses>> listOfNotMyCourses(@PathVariable("username") String username){
        log.info("[Student-Controller] Listing the not my courses");
        return studentService.listOfNotMyCourses(username);
    }

    /**Törli a jelentkezést*/
    @DeleteMapping(path = "/delete/{sid}/{cid}")
    public ResponseEntity<?> deleteAppointment(@PathVariable("sid") Long studentId, @PathVariable("cid") Long courseId){
        log.info("[Student-Controller] Deleted appointment for student (" + studentId + ") and course (" + courseId + ")");
        return studentService.deleteAppointment(studentId, courseId);
    }

}
