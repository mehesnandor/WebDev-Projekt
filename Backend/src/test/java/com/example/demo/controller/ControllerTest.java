package com.example.demo.controller;

import com.example.demo.model.Contacts;
import com.example.demo.model.Roles;
import com.example.demo.model.Users;
import com.example.demo.service.Services;
import lombok.Data;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(Controller.class)
@Data
public class ControllerTest {

    @Autowired
    private final MockMvc mvc;

    @Autowired
    private final Services services;

    Users userT = new Users(
            1L,
            "T",
            "Aa12345!",
            Roles.TEACHER,
            new Contacts(
                    "t@t.t"
            )
    );

    Users userS = new Users(
            2L,
            "S",
            "Aa12345!",
            Roles.STUDENT,
            new Contacts(
                    "s@s.s"
            )
    );

    Users userA = new Users(
            3L,
            "A",
            "Aa12345!",
            Roles.ADMIN,
            new Contacts(
                    "a@a.a"
            )
    );


    @Test
    public void getUserDataByUsername(String username) throws Exception {
        mvc.perform(get("/user_data/{username}"))
                .andExpect(status().isOk())
                .andExpect(content().json("[\"Math\",\"Science\",\"History\"]"));
    }

    
    public void updateUserData(Users user) {

    }

    
    public void deleteUserAccount(String username) {

    }

    
    public void register(Users user) {

    }

    
    public void verify(Users user) {

    }
}
