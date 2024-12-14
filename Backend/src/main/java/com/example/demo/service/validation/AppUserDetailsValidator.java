package com.example.demo.service.validation;

import com.example.demo.model.Contacts;
import com.example.demo.model.Users;
import com.example.demo.repo.ContactsRepo;
import com.example.demo.repo.UsersRepo;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Data
@Slf4j
@Service
public class AppUserDetailsValidator {

    @Autowired
    private final UsersRepo usersRepo;

    @Autowired
    private final ContactsRepo contactsRepo;

    //user test

    public boolean isUserDetailsValid(Users user){

        if (!PasswordValidator.isValidPassword(user.getPassword())){
            log.error("[AppUserDetailsValidator] Error, password is incorrect: " + user.getPassword());
            return false;
        }

        return isNotExistsUsersUsername(user.getUsername()) && isContactDetailsValid(user.getContact());
    }

    public boolean isNotExistsUsersUsername(String username){

        if (usersRepo.findByUsername(username) != null) {
            log.error("[AppUserDetailsValidator] User " + username + " already exists");
            return false;
        }

        return true;

    }

    //contact test

    public boolean isContactDetailsValid(Contacts contact){
        return isNotExistsContactEmail(contact.getEmail()) || isNotExistsContactPhone(contact.getPhone());
    }

    public boolean isNotExistsContactEmail(String email){

        if (contactsRepo.findByEmail(email) != null && email != null) {
            log.error("[AppUserDetailsValidator] Error, email exists: " + email);
            return false;
        }

        return true;
    }

    public boolean isNotExistsContactPhone(String phone){

        if (contactsRepo.findByEmail(phone) != null && phone != null) {
            log.error("[AppUserDetailsValidator] Error, phone exists: " + phone);
            return false;
        }

        return true;
    }

}
