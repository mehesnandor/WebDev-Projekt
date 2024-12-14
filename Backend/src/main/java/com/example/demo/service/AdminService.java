package com.example.demo.service;

import com.example.demo.model.Courses;
import com.example.demo.model.Roles;
import com.example.demo.model.Users;
import com.example.demo.repo.ContactsRepo;
import com.example.demo.repo.CoursesRepo;
import com.example.demo.repo.UsersRepo;
import com.example.demo.service.interfaces.AdminsServicesInt;
import com.example.demo.service.validation.AppUserDetailsValidator;
import com.example.demo.service.validation.CourseDetailsValidator;
import com.example.demo.service.validation.PasswordValidator;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Data
@Slf4j
public class AdminService implements AdminsServicesInt {

    @Autowired
    private final TeacherService teacherService;

    @Autowired
    private final AppUserDetailsValidator appUserDetailsValidator;

    @Autowired
    private final CourseDetailsValidator courseDetailsValidator;

    @Autowired
    private final UsersRepo usersRepo;

    @Autowired
    private final ContactsRepo contactsRepo;

    @Autowired
    private final CoursesRepo coursesRepo;

    @Override
    public ResponseEntity<?> createUser(Users user) {

        user.setUserId(null);

        if (user.getUsername() != null) {
            if (user.getUsername().length() >= 4) {
                if (usersRepo.findByUsername(user.getUsername()) != null) {
                    log.error("[Services] User with this username " + user.getUsername() + " exists! ");
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
                }
            }else{
                log.error("[Services] Username {} have to be more or equle than 4 characters!", user.getUsername());
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }
        }else {
            log.error("[Services] Username can not be null!");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }


        if (user.getPassword() != null) {
            if (!PasswordValidator.isValidPassword(user.getPassword())) {
                log.error("[Services] Password " + user.getPassword() + " is incorect or null! ");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }

            user.setPassword(new BCryptPasswordEncoder(12).encode(user.getPassword()));
        }else{
            log.error("[Services] Password can not be null!");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        if (user.getRole() != null) {
            if (!List.of(Roles.ROLE_STUDENT, Roles.ROLE_TEACHER, Roles.ROLE_ADMIN).contains(user.getRole())) {
                log.error("[Admin-Service] Roles only can be one of these: Role_STUDNET, ROLE_TEACHER, ROLE_ADMIN!");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }
        }else{
            log.error("[Admin-Service] Role can not be null!");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }


        if (user.getContact() != null){

            user.getContact().setContactId(null);

            if ((user.getContact().getEmail() != null || user.getContact().getPhone() != null)) {

                if (user.getContact().getEmail() != null) {
                    if (contactsRepo.findByEmail(user.getContact().getEmail()) != null) {
                        log.error("[Services] User with this email " + user.getContact().getEmail() + " exists! ");
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
                    }
                }

                if (user.getContact().getPhone() != null) {
                    if (contactsRepo.findByPhone(user.getContact().getPhone()) != null) {
                        log.error("[Services] User with this phone " + user.getContact().getPhone() + " exists! ");
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
                    }
                }

            }

            log.info("[Services] Registering a new contact for user " + user.getUsername() + " .  The contacts are " + user.getContact());
            contactsRepo.save(user.getContact());

        }

        log.info("[Services] Registering a new user " + user);
        usersRepo.save(user);
        return ResponseEntity.status(HttpStatus.OK).body(user.getData());
    }

    @Override
    public ResponseEntity<?> createCourse(Courses course) {

        course.setCourseId(null);
        course.setDuration(null);

        if (usersRepo.findById(course.getTeacher().getUserId()).isEmpty()){
            log.error("[Admin-Service] Course with this name exists! ");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        Users teacher = usersRepo.findById(course.getTeacher().getUserId()).get();

        course.setTeacher(teacher);
        log.info("[Admin-Services] Setting the teacher (" + teacher + ") to the course: " + course);


        if (coursesRepo.findByCourseName(course.getCourseName()) != null) {
            log.error("[Admin-Service] Course with this name exists! ");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        if (course.getCapacity() < 1 || course.getCapacity() > 50) {
            log.error("[Admin-Service] The capacity have to be between 1 and 50! ");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        log.info("[Admin-Services] Saving a course: " + course);
        return ResponseEntity.status(HttpStatus.OK).body(coursesRepo.save(course).getData());
    }

    //---

    @Override
    public ResponseEntity<List<Users>> getAllUsers() {
        log.info("[Admin-Service] Get All Users");
        List<Users> users = usersRepo.findAll();
        users.forEach(Users::getData);

        return ResponseEntity.status(HttpStatus.OK).body(users);
    }

    @Override
    public ResponseEntity<List<Courses>> getAllCourses() {
        log.info("[Admin-Service] Get All Courses");

        List<Courses> courses = coursesRepo.findAll();
        courses.forEach(Courses::getData);

        return ResponseEntity.status(HttpStatus.OK).body(courses);
    }

    @Override
    public ResponseEntity<Courses> getCourseById(Long id) {

        if (coursesRepo.findById(id).isEmpty()){
            log.error("[Admin-Service] Course with id " + id + "not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        log.info("[Admin-Service] Get Course by ID " + id);
        return ResponseEntity.status(HttpStatus.OK).body(coursesRepo.findById(id).get().getData());

    }

    @Override
    public ResponseEntity<Users> getUserById(Long id) {

        if (usersRepo.findById(id).isEmpty()){
            log.error("[Admin-Service] User with id " + id + " not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        log.info("[Admin-Service] Get User by ID " + id);
        return ResponseEntity.status(HttpStatus.OK).body(usersRepo.findById(id).get().getData());
    }

    @Override
    public ResponseEntity<List<Users>> appointmentsForThisCourse(Long courseId) {

        if (coursesRepo.findById(courseId).isEmpty()){
            log.error("[Admin-Service] Course with id " + courseId + " not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        log.info("[Teacher-Services] Listing appointments for course with course ID " + courseId);

        List<Users> users = coursesRepo.findById(courseId).get().getStudentsList();
        users.forEach(Users::getData);

        return ResponseEntity.status(HttpStatus.CREATED).body(users);
    }

    //---

    @Override
    public ResponseEntity<?> updateUser(Users userIn) {

        if (usersRepo.findById(userIn.getUserId()).isEmpty()){
            log.error("[Admin-Services] User with this id " + userIn.getUserId() + " did not exists! ");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        Users user = usersRepo.findById(userIn.getUserId()).get();

        if (userIn.getUsername() != null) {
            if (userIn.getUsername().length() >= 4) {
                if (usersRepo.findByUsername(userIn.getUsername()) != null) {
                    log.error("[Admin-Service] User with this username " + userIn.getUsername() + " exists! ");
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
                }
            }else{
                log.error("[Admin-Service] Username {} have to be more or equle than 4 characters!", userIn.getUsername());
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }

            user.setUsername(userIn.getUsername());
        }

        if (userIn.getPassword() != null) {
            if (!PasswordValidator.isValidPassword(userIn.getPassword())) {
                log.error("[Admin-Service] Password " + user.getPassword() + " is incorect or null! ");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }

            user.setPassword(new BCryptPasswordEncoder(12).encode(userIn.getPassword()));
        }

        if (userIn.getRole() != null) {
            if (List.of(Roles.ROLE_STUDENT, Roles.ROLE_TEACHER, Roles.ROLE_ADMIN).contains(userIn.getRole())) {
                user.setRole(userIn.getRole());
            }else{
                log.error("[Admin-Service] Roles only can be one of these: Role_STUDNET, ROLE_TEACHER, ROLE_ADMIN!");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }
        }


        if (userIn.getContact() != null) {

            if ((user.getContact().getEmail() != null && user.getContact().getPhone() != null)) {

                if (userIn.getContact().getEmail() != null) {
                    if (contactsRepo.findByEmail(userIn.getContact().getEmail()) != null) {
                        log.error("[Admin-Service] User with this email " + userIn.getContact().getEmail() + " exists! ");
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
                    }
                    user.getContact().setEmail(userIn.getContact().getEmail());
                }

                if (userIn.getContact().getPhone() != null) {
                    if (contactsRepo.findByPhone(userIn.getContact().getPhone()) != null) {
                        log.error("[Admin-Service] User with this phone " + userIn.getContact().getPhone() + " exists! ");
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
                    }
                    user.getContact().setPhone(userIn.getContact().getPhone());
                }

                log.info("[Admin-Service] Registering a new contact for user " + user.getUsername() + " .  The contacts are " + user.getContact());
                contactsRepo.save(user.getContact());
            }
        }

        log.info("[Admin-Service] Registering a new user " + user);
        return ResponseEntity.status(HttpStatus.OK).body(usersRepo.save(user).getData());
    }

    @Override
    public ResponseEntity<?> updateCourse(Courses course) {


        if (coursesRepo.findById(course.getCourseId()).isEmpty()) {
            log.error("[Admin-Service] Course dose not exists with this ID! ");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        Courses newCourse = new Courses(coursesRepo.findById(course.getCourseId()).get());

        if (course.getCourseId() != null) {
            newCourse.setCourseId(course.getCourseId());
        }else{
            log.error("[Admin-Service] You need to specify a course ID! Can not be null and it have to exist ");
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
                    log.error("[Admin-Service] Course with this name exists! ");
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
                }

                Users teacher = usersRepo.findById(course.getTeacher().getUserId()).get();

                newCourse.setTeacher(teacher);
                log.info("[Admin-Services] Setting the teacher (" + teacher + ") to the course: " + course);
            }else{
                log.error("[Admin-Service] Teacher ID can not be null! ");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }
        }else{
            log.error("[Admin-Service] Course's teacher can not be null and you have to give an existing teacherID! ");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }


        if (course.getCourseName() != null) {
            if (coursesRepo.findByCourseName(course.getCourseName()) != null) {
                log.error("[Admin-Service] Course with this name exists! ");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }

            newCourse.setCourseName(course.getCourseName());
        }

        if (course.getCapacity() != null) {
            if (course.getCapacity() < 1 || course.getCapacity() > 50 || course.getCapacity() < newCourse.getStudentsList().size()) {
                log.error("[Admin-Service] The capacity have to be between 1 and 50! ");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }

            newCourse.setCapacity(course.getCapacity());
        }

        log.info("[Admin-Services] Updating the course " + newCourse);
        return ResponseEntity.status(HttpStatus.OK).body(coursesRepo.save(newCourse).getData());
    }

    //---

    @Override
    public ResponseEntity<?> deleteUser(Long uid) {  //MI AZ A LAZY EXAPTIONNNNNNNN

        if (usersRepo.findById(uid).isEmpty()){
            log.info("[Admin-Service] User Data for user with id " + uid + " not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        Users user = usersRepo.findById(uid).get();

        try {

            for (Courses course : user.getUser_appointments()) {
                course.getStudentsList().remove(user);
                coursesRepo.save(course);
            }

            // Törli a STUDENT jelentkezéseit
            user.getUser_appointments().clear();
            usersRepo.save(user);

            usersRepo.delete(user);
            log.info("[Admin-Service] User deleted!");
        } catch (Exception e) {
            log.error("[Admin-Service] Error during deletion: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

        return ResponseEntity.status(HttpStatus.OK).build();
    }

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
}
