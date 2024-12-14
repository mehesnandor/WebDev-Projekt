package com.example.demo.repo;

import com.example.demo.model.Courses;
import com.example.demo.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface CoursesRepo extends JpaRepository<Courses, Long> {

    Courses findByCourseName(String courseName);

    List<Courses> findByEndDateBefore(LocalDateTime endDate);
    List<Courses> findByStudentsListNotContains(Users students);

    boolean existsByCourseName(String courseName);

}
