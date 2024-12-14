package com.example.demo.service.validation;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class PasswordValidator {

    public static boolean isValidPassword(String password) {

        // Minimum hosszúság
        int minLength = 8;

        // Feltételek
        boolean hasUpperCase = false;
        boolean hasLowerCase = false;
        boolean hasDigit = false;
        boolean hasSpecialChar = false;

        // Speciális karakterek halmaza
        String specialCharacters = "!@#$%^&*()=+-";

        if (password == null || minLength > password.length()) {
            return false;
        }

        for (char c : password.toCharArray()) {
            if (Character.isUpperCase(c)) {
                //log.info("[Pass-Validator] Van nagy betü");
                hasUpperCase = true;
            } else if (Character.isLowerCase(c)) {
                //log.info("[Pass-Validator] Van kis betü");
                hasLowerCase = true;
            } else if (Character.isDigit(c)) {
                //log.info("[Pass-Validator] Van szám");
                hasDigit = true;
            } else if (specialCharacters.contains(String.valueOf(c))) {
                //log.info("[Pass-Validator] Van szimbólum");
                hasSpecialChar = true;
            }
        }

        if (hasUpperCase && hasLowerCase && hasDigit && hasSpecialChar){
            log.info("[Pass-Validator] Minden rendben");
        }else{
            log.error("[Pass-Validator] Nem megfelelő jelszó");
        }

        return hasUpperCase && hasLowerCase && hasDigit && hasSpecialChar;
    }

}
