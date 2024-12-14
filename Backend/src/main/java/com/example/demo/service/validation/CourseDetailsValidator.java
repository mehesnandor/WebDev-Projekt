package com.example.demo.service.validation;

import com.example.demo.model.Courses;
import com.example.demo.repo.CoursesRepo;
import com.example.demo.repo.UsersRepo;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Data
@Slf4j
public class CourseDetailsValidator {

    /**
    * 1.) Nincs még egy ugyan olyan nevü kurzus
    * 2.) Van-e hozzá tanár, mert kurzus nem létezhet tanár nélkül. Ha a tanár nem létezik az adatbázisban az nem jó
    */

    @Autowired
    private final AppUserDetailsValidator appUserDetailsValidator;

    @Autowired
    private final CoursesRepo coursesRepo;

    @Autowired
    private final UsersRepo usersRepo;

    public boolean isCourseDetailsValid(Courses course){

        return isNotSameCourseName(course.getCourseName()) && isTeacherLogicValid(course);
    }

    public boolean isTeacherLogicValid(Courses course){

        if (course.getTeacher() == null || course.getTeacher().getUserId() == null || usersRepo.findById(course.getTeacher().getUserId()).isEmpty()){
            log.error("[CourseDetailsValidator] Teacher is not exists or you not specified one! At least give a theacher with it's id or it's username!");
            return false;
        }

        return true;
    }

    public boolean isTeacherLogicValidForUpdate(Courses course){

        if (course.getTeacher() == null){
            log.error("[CourseDetailsValidator] Teacher can not be null!");
            return false;
        }

        if (
                   (course.getTeacher().getUserId() == null || usersRepo.findById(course.getTeacher().getUserId()).isEmpty())
                && (course.getTeacher().getUsername() == null || usersRepo.findByUsername(course.getTeacher().getUsername()) == null))
        {
            log.error("[CourseDetailsValidator] Give at least an id or a username!");
            return false;
        }

        if (course.getTeacher().getContact() != null){
            if (!appUserDetailsValidator.isContactDetailsValid(course.getTeacher().getContact())){
                log.error("[CourseDetailsValidator] Your contact details are not valid or exists!");
                return false;
            }
        }

        return true;
    }

    public boolean isNotSameCourseName(String courseName){

        if (coursesRepo.findByCourseName(courseName) != null) {
            log.error("[CourseDetailsValidator] Course with this name (" + courseName + ") already exists!");
            return false;
        }

        return true;
    }

}
