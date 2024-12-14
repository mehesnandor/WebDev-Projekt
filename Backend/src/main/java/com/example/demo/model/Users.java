package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@RequiredArgsConstructor

@Entity(name = "users")
@Table(name = "Users")
public class Users {

    @Valid

    @Id
    @SequenceGenerator(
            name = "users_seq",
            sequenceName = "users_seq",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "users_seq"
    )

    @Column(
            name = "userId",
            nullable = false,
            unique = true
    )
    private Long userId;

    @Column(
            name = "userName",
            nullable = false,
            unique = true
    )
    @NotBlank(message = "firstname can not be blank")
    private String username;

    @Column(
            name = "password",
            nullable = false
    )
    @NotBlank(message = "password can not be blank")
    private String password;

    @Column(
            name = "role",
            nullable = false
    )
    @Enumerated(EnumType.STRING)
    private Roles role;

    public Users(String username, String password, Roles role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }

    public Users(Long id, String username, String password, Roles role) {
        this.userId = id;
        this.username = username;
        this.password = password;
        this.role = role;
    }

    public Users(String username, String password) {
        this.username = username;
        this.password = password;
        this.role = Roles.ROLE_STUDENT;
    }

    public Users(Long id, String username, String password) {
        this.userId = id;
        this.username = username;
        this.password = password;
        this.role = Roles.ROLE_STUDENT;
    }

    public Users(String username, String password, Roles role, Contacts contacts) {
        this.username = username;
        this.password = password;
        this.role = role;
        this.contact = contacts;
    }

    public Users(Long id, String username, String password, Roles role, Contacts contacts) {
        this.userId = id;
        this.username = username;
        this.password = password;
        this.role = role;
        this.contact = contacts;
    }

    public Users(String username, String password, Contacts contacts) {
        this.username = username;
        this.password = password;
        this.role = Roles.ROLE_STUDENT;
        this.contact = contacts;
    }

    public Users(Long id, String username, String password, Contacts contacts) {
        this.userId = id;
        this.username = username;
        this.password = password;
        this.role = Roles.ROLE_STUDENT;
        this.contact = contacts;
    }

    // FKs

    @OneToOne(orphanRemoval = true)
    @JoinColumn(name = "contact_id")
    private Contacts contact;

    @OneToMany(mappedBy = "teacher", cascade = CascadeType.REMOVE)
    private List<Courses> teacherCourses;

    @ManyToMany(mappedBy = "studentsList")
    private List<Courses> user_appointments;

    @JsonIgnore
    public Users getData(){

        this.teacherCourses = new ArrayList<>();
        this.user_appointments = new ArrayList<>();

        return this;

    }


    @Override
    public String toString() {


        try {
            if (this.teacherCourses != null) {
                this.teacherCourses.forEach(Courses::getData);
            }
            if (this.user_appointments != null) {
                this.user_appointments.forEach(Courses::getData);
            }
        } catch (Exception e) {
            return "Users{" +
                    "userId=" + userId +
                    ", username='" + username + '\'' +
                    ", password='" + password + '\'' +
                    ", role=" + role +
                    ", contact=" + contact +
                    '}';
        }


        return "Users{" +
                "userId=" + userId +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", role=" + role +
                ", contact=" + contact +
                ", teacherCourses=" + teacherCourses +
                ", user_appointments=" + user_appointments +
                '}';
    }
}
