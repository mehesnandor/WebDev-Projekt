package com.example.demo.service;

import com.example.demo.model.Courses;
import com.example.demo.model.Users;
import com.example.demo.repo.ContactsRepo;
import com.example.demo.repo.CoursesRepo;
import com.example.demo.repo.UsersRepo;
import com.example.demo.service.interfaces.TearchersServicesInt;
import com.example.demo.service.validation.CourseDetailsValidator;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Data
@Slf4j
public class TeacherService implements TearchersServicesInt {

    @Autowired
    private final UsersRepo usersRepo;

    @Autowired
    private final CoursesRepo coursesRepo;
    @Autowired
    private ContactsRepo contactsRepo;

    /**
     * Menti az adatbázisba a kurzust
     * */

    @Override
    public ResponseEntity<Courses> saveCourse(Courses course) {

        course.setCourseId(null);
        course.setDuration(null);

        if (usersRepo.findById(course.getTeacher().getUserId()).isEmpty()){
            log.error("[Teacher-Service] Course with this name exists! ");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        Users teacher = usersRepo.findById(course.getTeacher().getUserId()).get();

        course.setTeacher(teacher);
        log.info("[Teacher-Services] Setting the teacher (" + teacher + ") to the course: " + course);


        if (coursesRepo.findByCourseName(course.getCourseName()) != null) {
            log.error("[Teacher-Service] Course with this name exists! ");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        if (course.getCapacity() < 1 || course.getCapacity() > 50) {
            log.error("[Teacher-Service] The capacity have to be between 1 and 50! ");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        log.info("[Teacher-Services] Saving a course: " + course);
        return ResponseEntity.status(HttpStatus.OK).body(coursesRepo.save(course).getData());
    }

    /**
     * Listázza a tanárhoz tartozó kurzusokat
     * */

    @Override
    public ResponseEntity<List<Courses>> listOfMyCourses(Long teacherId) {

        log.info("[Teacher-Services] Listing the courses for teacher with ID " + teacherId);

        if (usersRepo.findById(teacherId).isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        Users teacher = usersRepo.findById(teacherId).get();

        List<Courses> teacherCourses = teacher.getTeacherCourses();
        teacherCourses.forEach(courses -> courses.setStudentsList(new ArrayList<>()));
        teacherCourses.forEach(Courses::getData);

        log.info("[Teacher-Services] Teacher found: " + teacher);

        return ResponseEntity.status(HttpStatus.OK).body(teacherCourses);
    }

    /**
     * Listázza a kurzus adatait
     * */

    @Override
    public ResponseEntity<Courses> detailsOfACourse(Long courseId) {

        log.info("[Teacher-Services] Listing the course details for course with ID " + courseId);

        if(coursesRepo.findById(courseId).isEmpty()){
            log.error("[Teacher-Services] Course with id " + courseId + " not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        Courses course = coursesRepo.findById(courseId).get();

        log.info("[Teacher-Services] Details of the course {}", course);

        List<Users> students_appointments = appointmentsForThisCourse(courseId).getBody();

        // for (Users student_appointment : students_appointments) {/*delete every depth more than 1, so User.Appointments, TeacherCourses*/}

        log.info("[Teacher-Services] Appointments: " + students_appointments);

        return ResponseEntity.status(HttpStatus.OK).body(course.getData());
    }

    /**
     * Listázza a kurzushoz tartozó tanulókat
     * */

    @Override
    public ResponseEntity<List<Users>> appointmentsForThisCourse(Long courseId) {

        if (coursesRepo.findById(courseId).isEmpty()) {
            log.error("[Teacher-Services] Course with ID " + courseId + " does not exist");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        log.info("[Teacher-Services] Listing appointments for course with course ID " + courseId);

        List<Users> users = coursesRepo.findById(courseId).get().getStudentsList();
        users.forEach(Users::getData);

        return ResponseEntity.status(HttpStatus.OK).body(users);
    }

    /**
     * Frisiti egy kurzus adatait
     * */

    @Override
    public ResponseEntity<?> updateThisCourse(Courses course) {

        if (coursesRepo.findById(course.getCourseId()).isEmpty()) {
            log.error("[Teacher-Service] Course dose not exists with this ID! ");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        Courses newCourse = new Courses(coursesRepo.findById(course.getCourseId()).get());

        if (course.getCourseId() != null) {
            newCourse.setCourseId(course.getCourseId());
        }else{
            log.error("[Teacher-Service] You need to specify a course ID! Can not be null and it have to exist ");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        if (course.getCourseDescription() != null) {
            newCourse.setCourseDescription(course.getCourseDescription());
        }

        if (course.getStartDate() != null) {
            newCourse.setStartDate(course.getStartDate());
        }

        if (course.getEndDate() != null) {
            newCourse.setEndDate(course.getEndDate());
        }

        newCourse.setDuration(null);


        if (course.getTeacher() != null) {
            if (course.getTeacher().getUserId() != null) {
                if (usersRepo.findById(course.getTeacher().getUserId()).isEmpty()){
                    log.error("[Teacher-Service] Course with this name exists! ");
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
                }

                Users teacher = usersRepo.findById(course.getTeacher().getUserId()).get();

                newCourse.setTeacher(teacher);
                log.info("[Teacher-Services] Setting the teacher (" + teacher + ") to the course: " + course);
            }else{
                log.error("[Teacher-Service] Teacher ID can not be null! ");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }
        }else{
            log.error("[Teacher-Service] Course's teacher can not be null and you have to give an existing teacherID! ");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }


        if (course.getCourseName() != null) {
            if (coursesRepo.findByCourseName(course.getCourseName()) != null) {
                log.error("[Teacher-Service] Course with this name exists! ");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }

            newCourse.setCourseName(course.getCourseName());
        }

        if (course.getCapacity() != null) {
            if (course.getCapacity() < 1 || course.getCapacity() > 50 || course.getCapacity() < newCourse.getStudentsList().size()) {
                log.error("[Teacher-Service] The capacity have to be between 1 and 50! ");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }

            newCourse.setCapacity(course.getCapacity());
        }

        log.info("[Teacher-Services] Updating the course " + newCourse);
        return ResponseEntity.status(HttpStatus.OK).body(coursesRepo.save(newCourse).getData());
    }

    /**
     * Töröl egy kurzust
     * */

    @Override
    public ResponseEntity<?> deleteCourse(Long courseId) {

        log.info("[Teacher-Services] Deleting the course with ID " + courseId);

        if (coursesRepo.findById(courseId).isEmpty()) {
            log.error("[Teacher-Services] Course with ID " + courseId + " does not exist");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        Courses course = coursesRepo.findById(courseId).get();

        log.info("[Teacher-Services] Deleting the course " + course);

        Users teacher = course.getTeacher();
        teacher.getTeacherCourses().remove(course);
        usersRepo.save(teacher);

        course.setTeacher(null);

        log.info("[Teacher-Services] Deleting the appointments of course with id " + courseId);

        coursesRepo.delete(course);

        log.info("[Teacher-Services] Deleted the course with " + course);

        return ResponseEntity.status(HttpStatus.OK).build();

    }

    /**
     * Töröl egy jelentkezést a kurzusról
     * */

    @Override
    public ResponseEntity<?> deleteAppointment(Long courseId, Long studentId) {

        log.info("[Teacher-Services] Deleting appointment with course ID " + courseId + " and student ID " + studentId);

        if (usersRepo.findById(studentId).isEmpty()){
            log.error("[Teacher-Services] Student with id " + studentId + " not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        Users student = usersRepo.findById(studentId).get();

        if (coursesRepo.findById(courseId).isEmpty()){
            log.error("[Teacher-Services] Course with id " + courseId + " not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        Courses course = coursesRepo.findById(courseId).get();

        log.info("[Teacher-Services] Deleting appointment with found course " + course + " and found student " + student);

        student.getUser_appointments().remove(course);
        course.getStudentsList().remove(student);

        log.info("[Teacher-Services] Deleted appointment with course " + course + " and student " + student);

        coursesRepo.save(course);
        usersRepo.save(student);

        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
