package com.example.demo.service;

import com.example.demo.model.Courses;
import com.example.demo.repo.CoursesRepo;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@EnableScheduling
@Data
@Slf4j
public class ScheduledTasks {

    @Autowired
    private final CoursesRepo coursesRepo;

    @Autowired
    private final TeacherService teacherService;

    /**
     * Naponta törli a lejárt kurzusokat az adatbázisból
     * */

    @Scheduled(cron = "0 0 0 * * ?")
    public void deleteExpiredAppointments(){

        log.info("[ScheduleTasks] Deleting Expired Appointments");

        List<Courses> expiredCourses = coursesRepo.findByEndDateBefore(LocalDateTime.now());

        log.info("[ScheduleTasks] Found Expired Appointments: {}", expiredCourses);

        for (Courses courses : expiredCourses) {
            teacherService.deleteCourse(courses.getCourseId());
        }

        log.info("[ScheduleTasks] Deleted Expired Appointments");

    }

}
