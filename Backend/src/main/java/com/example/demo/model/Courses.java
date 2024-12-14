package com.example.demo.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.*;
import org.hibernate.validator.constraints.Range;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

@Entity(name = "courses")
@Table(name = "Courses")
public class Courses {

    @Valid

    @Id
    @SequenceGenerator(
            name = "courses_seq",
            sequenceName = "courses_seq",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "courses_seq"
    )

    @Column(
            name = "courseId",
            nullable = false,
            unique = true
    )
    private Long courseId;

    @Column(
            name = "courseName",
            nullable = false,
            unique = true
    )
    @NotBlank(message = "coursename can not be blank")
    private String courseName;

    @Column(
            name = "courseDescription"
    )
    @Size(min = 0, max = 255)
    private String courseDescription;

    @Column(
            name = "capacity",
            nullable = false
    )
    @Range(min = 1, max = 50, message = "Capacity must be between 1 and 50")
    @NotNull(message = "capacity can not be blank")
    private Integer capacity;

    @Column(
            name = "startDate",
            nullable = false
    )
    @FutureOrPresent(message = "Csak jelen és jövőbeli időket adhatsz meg")
    private LocalDateTime startDate;

    @Column(
            name = "endDate",
            nullable = false
    )
    @FutureOrPresent(message = "Csak jelen és jövőbeli időket adhatsz meg")
    private LocalDateTime endDate;

    @Transient
    private Integer duration;

    public Integer getDuration() {
        return startDate != null && endDate != null
                ? (int) ChronoUnit.MINUTES.between(startDate, endDate)
                : null;
    }

    // FKs

    @ManyToOne(cascade = CascadeType.REMOVE, fetch = FetchType.EAGER)
    @JoinColumn(name = "teacher")
    private Users teacher;

    @ManyToMany
    @JoinTable(
            name = "appointments_to_courses",
            joinColumns = @JoinColumn(name = "courseId"),
            inverseJoinColumns = @JoinColumn(name = "userId")
    )
    private List<Users> studentsList;



    //constructors

    public Courses(String courseName, String courseDescription, Integer capacity, LocalDateTime startDate, LocalDateTime endDate) {
        this.courseName = courseName;
        this.courseDescription = courseDescription;
        this.capacity = capacity;
        this.startDate = startDate;
        this.endDate = endDate;
        this.duration = getDuration();
    }

    public Courses(String courseName, String courseDescription, Integer capacity, LocalDateTime startDate, LocalDateTime endDate, Users teacher_id) {
        this.courseName = courseName;
        this.courseDescription = courseDescription;
        this.capacity = capacity;
        this.startDate = startDate;
        this.endDate = endDate;
        this.duration = getDuration();
        this.teacher = teacher_id;
    }

    public Courses(Courses course) {
        this.courseId = course.getCourseId();
        this.courseName = course.getCourseName();
        this.courseDescription = course.getCourseDescription();
        this.capacity = course.getCapacity();
        this.startDate = course.getStartDate();
        this.endDate = course.getEndDate();
        this.duration = course.getDuration();
        this.teacher = course.getTeacher();
        this.studentsList = course.getStudentsList();
    }



    @JsonIgnore
    public Courses getData(){

        this.teacher.setTeacherCourses(List.of());
        this.teacher.setUser_appointments(List.of());

        if (this.studentsList != null) {
            for (Users user: this.studentsList) {
                user.getData();
            }
        }

        return this;

    }

    @Override
    public String toString() {
        if(this.studentsList != null) {
            this.studentsList.forEach(Users::getData);
        }

        if (teacher != null){
            this.teacher.getData();
        }

        return "Courses{" +
                "courseId=" + courseId +
                ", courseName='" + courseName + '\'' +
                ", courseDescription='" + courseDescription + '\'' +
                ", capacity=" + capacity +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", duration=" + duration +
                ", teacher=" + teacher +
                ", studentsList=" + studentsList +
                '}';
    }
}
