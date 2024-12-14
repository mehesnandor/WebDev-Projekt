package com.example.demo.controller;


import com.example.demo.model.Courses;
import com.example.demo.model.Users;
import com.example.demo.service.TeacherService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/api/v1/management/teacher")
@RequiredArgsConstructor
@Slf4j
public class TeachersController{

    @Autowired
    private final TeacherService teacherService;

    /**
     * Kurzus létrehozása
     * */

    @PostMapping(path = "/create_course")
    public ResponseEntity<Courses> saveCourse(@RequestBody Courses course) {
        log.info("[Teacher-Controller] Saving course: " + course );
        return teacherService.saveCourse(course);
    }

    /**
     * Listázza a kurzusra jelentkezett tanulókat
     * */

    @GetMapping(path = "/appointmentslist/{cid}")
    public ResponseEntity<List<Users>> appointmentsForThisCourse(@PathVariable("cid") Long courseId) {
        log.info("[Teacher-Controller] Listing appointments for courseID " + courseId);
        return teacherService.appointmentsForThisCourse(courseId);
    }

    /**
     * Listázza a tanár kurzusait
     * */

    @GetMapping(path = "/mycourses/{tid}")
    public ResponseEntity<List<Courses>> listOfMyCourses(@PathVariable("tid") Long teacherId) {
        log.info("[Teacher-Controller] Listing courses for teacherID " + teacherId);
        return teacherService.listOfMyCourses(teacherId);
    }

    /**
     * Listázza egy kurzus adatait
     * */

    @GetMapping(path = "/details/{cid}")
    public ResponseEntity<Courses> detailsOfACourse(@PathVariable("cid") Long courseId) {
        log.info("[Teacher-Controller] Listing course details for courseID " + courseId);
        return teacherService.detailsOfACourse(courseId);
    }

    /**
     * Frisiti egy kurzus adatait
     * */

    @PutMapping(path = "/update")
    public ResponseEntity<?> updateThisCourse(@RequestBody Courses course) {
        log.info("[Teacher-Controller] Updating course " + course);
        return teacherService.updateThisCourse(course);
    }

    /**
     * Töröl egy kurzust
     * */

    @DeleteMapping(path = "/delete/course/{cid}")
    public ResponseEntity<?> deleteCourse(@PathVariable("cid") Long courseId) {
        log.info("[Teacher-Controller] Deleting course with courseID " + courseId);
        return teacherService.deleteCourse(courseId);
    }

    /**
     * Törli a tanuló és a kurzus közötti kapcsolatot, vagyis a kurzusra való jelentkezését
     * */

    @DeleteMapping(path = "/delete/appointment/{cid}/{sid}")
    public ResponseEntity<?> deleteAppointment(@PathVariable("cid") Long courseId, @PathVariable("sid") Long studentId) {
        log.info("[Teacher-Controller] Deleting appointment (" + studentId + ") for course (" + courseId + ") ");
        return teacherService.deleteAppointment(courseId, studentId);
    }
}
