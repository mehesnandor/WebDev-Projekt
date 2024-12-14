package com.example.demo.controller;

import com.example.demo.model.Users;
import com.example.demo.service.Services;
import io.swagger.v3.oas.annotations.Operation;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@Data
@Slf4j
public class Controller {

    /**
     * Saját adatok kezelésére és login-regisztáció
     * */

    @Autowired
    private final Services services;

    /**
     *  Bejelentkezés
     * */

    @PostMapping(path = "/login")
    public String login(@RequestBody Users user){
        log.info("[Controller] Login for " + user.getUsername());
        return services.verify(user);
    }

    /**
     * Felhasználó regisztrálása
     * */

    //Create a user
    @PostMapping(path = "/register")
    public ResponseEntity<?> registration(@RequestBody Users user){
        log.info("[Controller] Registering User as " + user);
        return services.register(user);
    }

    //------UserData

    /**
     * Felhasználó adatainak lekérdezése felhasználó név alapján
     * */

    //Read
    @GetMapping(path = "/user_data/{username}")
    public ResponseEntity<?> userData(@PathVariable("username") String username) {
        log.info("[Controller] User Data for " + username);
        return services.getUserDataByUsername(username);
    }

    /**
     * Felhasználói adatok frissitése
     * */
    //Update
    @PutMapping(path = "/user_data/update")
    public ResponseEntity<?> update(@RequestBody Users user){
        log.info("[Controller] Update User to " + user);
        return services.updateUserData(user);
    }

    /**
     * Felhasználó törlése felhasználó név alapján.
     * Feltételezük, hogy a felhasználó név az egyedi.
     * */

    @DeleteMapping(path = "/user_data/delete/{uid}")
    public ResponseEntity<?> delete(@PathVariable("uid") Long uid){
        log.info("[Controller] Deleting User with userID " + uid);
        return services.deleteUserAccount(uid);
    }

    /**
     * Validációs hibák kezelője
     * */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        log.error("[Controller] Bad_Request error messages:" + errors);
        return errors;
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<Object> handleResponseStatusException(ResponseStatusException ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("timestamp", LocalDateTime.now());
        response.put("status", ex.getStatusCode().value());
        response.put("error", ex.getReason());
        response.put("message", "Custom message: " + ex.getReason());

        return new ResponseEntity<>(response, ex.getStatusCode());
    }

}
