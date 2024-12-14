package com.example.demo.controller;

import com.example.demo.model.Courses;
import com.example.demo.model.Users;
import com.example.demo.service.AdminService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/api/v1/management/admin")
@RequiredArgsConstructor
@Slf4j
public class AdminsController {

    @Autowired
    private final AdminService adminService;

    /**
     * Létrehoz egy felhasználót
     * */

    @PostMapping(path = "/create_user")
    public ResponseEntity<?> createUser(@RequestBody Users user) {
        log.info("[Admin-Controller] Create user: " + user);
        return adminService.createUser(user);
    }

    /**
     * Létrehoz egy kurzust
     * Feltételezem, hogy a Course-ban megkapom legalább a teacher mint Users ID-ját
     * */

    @PostMapping(path = "/create_course/{tid}")
    public ResponseEntity<?> createCourse(@RequestBody Courses course) {
        log.info("[Admin-Controller] Create course: " + course);
        return adminService.createCourse(course);
    }

    //---

    /**
     * Listázza az összes felhasználót
     * */

    @GetMapping(path = "/all_users")
    public ResponseEntity<List<Users>> getAllUsers() {
        log.info("[Admin-Controller] Get all users");
        return adminService.getAllUsers();
    }

    /**
     * Létrehoz az összes kurzust
     * */

    @GetMapping(path = "/all_courses")
    public ResponseEntity<List<Courses>> getAllCourses() {
        log.info("[Admin-Controller] Get all courses");
        return adminService.getAllCourses();
    }

    /**
     * Listáz egy kurzust
     * */

    @GetMapping(path = "/course/{cid}")
    public ResponseEntity<Courses> getCourseById(@PathVariable("cid") Long id) {
        log.info("[Admin-Controller] Get course by id: " + id);
        return adminService.getCourseById(id);
    }

    /**
     * Listáz egy felhasználót
     * */

    @GetMapping(path = "/user/{uid}")
    public ResponseEntity<Users> getUserById(@PathVariable("uid") Long id) {
        log.error("[Admin-Controller] User with id " + id + "not found");
        return adminService.getUserById(id);
    }

    //---

    /**
     * Frissiti egy felhasználó adatait
     * */

    @PutMapping(path = "/user_update")
    public ResponseEntity<?> updateUser(@RequestBody Users user) {
        log.info("[Admin-Controller] Update user: " + user.toString());
        return adminService.updateUser(user);
    }

    /**
     * Frissiti egy kurzus adatait
     * */

    @PutMapping(path = "/course_update")
    public ResponseEntity<?> updateCourse(@RequestBody Courses course) {
        log.info("[Admin-Controller] Update course: " + course.toString());
        return adminService.updateCourse(course);
    }

    //---

    /**
     * Töröl egy felhasználót
     * */

    @DeleteMapping(path = "/user/delete/{uid}")
    public ResponseEntity<?> deleteUser(@PathVariable("uid") Long id) {
        log.info("[Admin-Controller] Delete user by id: " + id);
        return adminService.deleteUser(id);
    }

    /**
     * Töröl egy kurzust
     * */

    @DeleteMapping(path = "/course/delete/{cid}")
    public ResponseEntity<?> deleteCourse(@PathVariable("cid") Long id) {
        log.info("[Admin-Controller] Delete course by id: " + id);
        return adminService.deleteCourse(id);
    }

}
