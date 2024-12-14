package com.example.demo.service;

import com.example.demo.model.Courses;
import com.example.demo.model.Users;
import com.example.demo.repo.CoursesRepo;
import com.example.demo.repo.UsersRepo;
import com.example.demo.service.interfaces.StudentsServicesInt;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
//@Transactional
@RequiredArgsConstructor
@Slf4j
public class StudentService implements StudentsServicesInt {

    @Autowired
    private TeacherService teacherService;

    @Autowired
    private final CoursesRepo coursesRepo;

    @Autowired
    private final UsersRepo usersRepo;

    /**
     * Egy felhasználóhoz rendel egy kurzust
     * */

    @Override
    public ResponseEntity<?> makeAnAppointment(Long studentId, Long courseId) {

        if (coursesRepo.findById(courseId).isEmpty()){
            log.error("[Student-Service] Course not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        if (usersRepo.findById(studentId).isEmpty()){
            log.error("[Student-Service] User not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        Users student = usersRepo.findById(studentId).get();
        Courses course = coursesRepo.findById(courseId).get();

        log.info("[Student-Service] Making an appointment");

        if (!student.getUser_appointments().contains(course)) { // Hogy ne szerepeljen egy kurzus többször

            if (course.getCapacity() < teacherService.appointmentsForThisCourse(course.getCourseId()).getBody().size() + 1){ // ne lépje át a max kapacitást
                log.error("[Student-Controller] Out of capacity. You were late. Try it next time or with other courses!");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }

            student.getUser_appointments().add(course);
            course.getStudentsList().add(student);

            coursesRepo.save(course);
            usersRepo.save(student);

            log.info("[Student-Service] Successfully maked an appointment " + student.getUser_appointments().toString());

            return ResponseEntity.status(HttpStatus.OK).build();

        }

        log.error("Course (" + course + ") already added to student " + student.getUser_appointments().toString());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();

    }

    /**
     * Listázza a tanulóhoz tartozó kurzusokat
     * */

    @Override
    public ResponseEntity<List<Courses>> listOfMyCourses(Long studentId) {

        if (usersRepo.findById(studentId).isEmpty()){
            log.error("[Student-Service] Course not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        log.info("[Student-Service] Listing My Courses");
        List<Courses> mycourses = usersRepo.findById(studentId).get().getUser_appointments();
        mycourses.forEach(Courses::getData);
        return ResponseEntity.status(HttpStatus.OK).body(mycourses);
    }

    /**
     * Listázza a nem ahoz a tanulóhoz tartozó kurzusokat
     * */

    @Override
    public ResponseEntity<List<Courses>> listOfNotMyCourses(String username) {
        log.info("[Student-Service] Listing Not My Courses");
        List<Courses> notmycourses = coursesRepo.findByStudentsListNotContains(usersRepo.findByUsername(username));
        notmycourses.forEach(Courses::getData);
        return ResponseEntity.status(HttpStatus.OK).body(notmycourses);
    }

    @Override
    public ResponseEntity<?> deleteAppointment(Long studentId, Long courseId) {

        if (usersRepo.findById(studentId).isEmpty()){
            log.error("[Student-Service] User not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        if (coursesRepo.findById(courseId).isEmpty()){
            log.error("[Student-Service] Course not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        Courses course = coursesRepo.findById(courseId).get();
        Users student = usersRepo.findById(studentId).get();

        log.info("[Student-Service] Deleting an appointment with id: " + course + " and student: " + student);

        student.getUser_appointments().remove(course);
        course.getStudentsList().remove(student);

        usersRepo.save(student);
        coursesRepo.save(course);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
