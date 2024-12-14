package com.example.demo.config;

import com.example.demo.model.Contacts;
import com.example.demo.model.Courses;
import com.example.demo.model.Roles;
import com.example.demo.model.Users;
import com.example.demo.repo.CoursesRepo;
import com.example.demo.repo.UsersRepo;
import com.example.demo.service.AdminService;
import com.github.javafaker.Faker;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;


@Configuration
@RequiredArgsConstructor
public class Config {

    //Services

    @Autowired
    private final AdminService adminService;


    // Repos

    @Autowired
    private final CoursesRepo coursesRepo;

    @Autowired
    private final UsersRepo usersRepo;


    // -----

    @Bean
    CommandLineRunner commandLineRunner() {
        return args -> {

            LocalDateTime now = LocalDateTime.now();

            Users user1 = new Users(
                    "Teacher",
                    "Aa12345!",
                    Roles.ROLE_TEACHER,
                    new Contacts(
                            "t@t.t"
                    )
            );

            Users user2 = new Users(
                    "Student",
                    "Aa12345!",
                    Roles.ROLE_STUDENT,
                    new Contacts(
                            "s@s.s"
                    )
            );

            Users user3 = new Users(
                    "Admin",
                    "Aa12345!",
                    Roles.ROLE_ADMIN,
                    new Contacts(
                            "a@a.a"
                    )
            );

            adminService.createUser(user1);
            adminService.createUser(user2);
            adminService.createUser(user3);

            Faker faker = new Faker();

            // Users

            for (int i = 0; i < 30; i++) {
                UserGenerator();
            }

            // Courses

            List<Users> teachers = usersRepo.findByRole(Roles.ROLE_TEACHER);

            //for (int i = 0; i < 10; i++) {

                for (Users teacher : teachers) {

                    int k = faker.number().numberBetween(1, 5);

                    for (int j = 0; j < k; j++) {
                        CourseGenerator(teacher);
                    }

                }

            //}

            // Appointments

           // makeSomeAppointments();

            List<Users> students = usersRepo.findByRole(Roles.ROLE_STUDENT);
            students.forEach(Users::getData);
            for (Users user : students) {

               // for (int i = 0; i < 3; i++) {
                    List<Courses> Allcourses = coursesRepo.findAll();

                    int k = faker.number().numberBetween(1, Allcourses.size());
                    for (int j = 0; j < k; j++) {

                        Courses course = Allcourses.get(faker.number().numberBetween(0, Allcourses.size()));

                       // if (!user.getUser_appointments().contains(course)) {
                            course.setStudentsList(List.of(user));
                            coursesRepo.save(course);

                            user.setUser_appointments(List.of(course));
                            usersRepo.save(user);
                       // }

                        Allcourses.remove(course);

                    }
                //}

            }

            System.out.println("[Config] Test Data Insertion are done! Started at: " + now + " finished at: " + LocalDateTime.now() + ". Duration: " + getDuration(now, LocalDateTime.now()));

        };
    }

    public Integer getDuration(LocalDateTime start, LocalDateTime end) {
        return (int) ChronoUnit.SECONDS.between(start, end);
    }


    public void UserGenerator(){

        Faker faker = new Faker();

        String username = faker.name().username();

        String password = faker.regexify("[a-zA-Z0-9!@#$%^&*()]{8,16}");

        Roles role = faker.options().option(Roles.class);

        System.out.println(role);

        String email = faker.internet().emailAddress();

        System.out.println(email);

        String phone = faker.regexify("(\\+\\d{1,3}[- ]?)?\\d{10}");

        Users user = new Users(
                username,
                password,
                role,
                new Contacts(
                        email,
                        phone
                )
        );

        adminService.createUser(user);

    }

    public void CourseGenerator(Users teacher) {

        Faker faker = new Faker();

        String courseName = faker.name().title();

        String courseDescription = faker.lorem().characters(250);

        Integer capacity = faker.number().numberBetween(1, 50);

        LocalDateTime startDate = faker.date().between(
                Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()),
                Date.from(LocalDateTime.now().plusMonths(2).atZone(ZoneId.systemDefault()).toInstant())
        ).toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();

        LocalDateTime endDate = faker.date().between(
                Date.from(LocalDateTime.now().plusMonths(2).atZone(ZoneId.systemDefault()).toInstant()),
                Date.from(LocalDateTime.now().plusMonths(3).atZone(ZoneId.systemDefault()).toInstant())
        ).toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();

        Courses course = new Courses(
                courseName,
                courseDescription,
                capacity,
                startDate,
                endDate,
                teacher
        );

        adminService.createCourse(course); //azért lehetséges a duplikált név, sértve ezzel a unique megszoritást, mert nem a service-n futtatad át, buta buta buuuuuutttaaaaaa, bazd meg
                                  // Most még kiszedtem a unique-ot, de legközelebb ezt kijavitod
    }

    public void makeSomeAppointments() {

        Faker faker = new Faker();

        List<Users> students = usersRepo.findByRole(Roles.ROLE_STUDENT);

        for (Users user : students) {

            List<Courses> courses = coursesRepo.findAll();

            int k = faker.number().numberBetween(0, courses.size());

            for (int i = 0; i < k; i++) {

                Courses course = courses.get(faker.random().nextInt(courses.size()));
                courses.remove(course);

                user.setUser_appointments(courses);
                usersRepo.save(user);

                course.setStudentsList(List.of(user));
                coursesRepo.save(course);
            }

        }

    }


}
