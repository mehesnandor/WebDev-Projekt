package com.example.demo.service.interfaces;

import com.example.demo.model.Users;
import org.springframework.http.ResponseEntity;

public interface ServicesInterface {

    ResponseEntity<Users> getUserDataByUsername(String username);
    ResponseEntity<Users> updateUserData(Users user);
    ResponseEntity<?> deleteUserAccount(Long uid);
    ResponseEntity<Users> register(Users user);
    String verify(Users user);
}
