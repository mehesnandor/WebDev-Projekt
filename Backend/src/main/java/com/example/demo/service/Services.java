package com.example.demo.service;


import com.example.demo.model.Courses;
import com.example.demo.model.Roles;
import com.example.demo.model.Users;
import com.example.demo.repo.ContactsRepo;
import com.example.demo.repo.CoursesRepo;
import com.example.demo.repo.UsersRepo;
import com.example.demo.security.JWTService;
import com.example.demo.service.interfaces.ServicesInterface;
import com.example.demo.service.validation.AppUserDetailsValidator;
import com.example.demo.service.validation.PasswordValidator;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Data
@Slf4j
public class Services implements ServicesInterface {

    @Autowired
    private final AuthenticationManager authManager;

    @Autowired
    private final JWTService jwtService;

    @Autowired
    private final AppUserDetailsValidator appUserDetailsValidator;

    @Autowired
    private final TeacherService teacherService;

    @Autowired
    private final UsersRepo usersRepo;

    @Autowired
    private final CoursesRepo coursesRepo;

    @Autowired
    private final ContactsRepo contactsRepo;

    /**
     * Felhasználó regisztrálása
     * */

    @Override
    public ResponseEntity<Users> register(Users user){

        user.setUserId(null);

        user.setRole(Roles.ROLE_STUDENT);

        if (user.getUsername() != null) {
            if (user.getUsername().length() >= 4) {
                if (usersRepo.findByUsername(user.getUsername()) != null) {
                    log.error("[Services] User with this username " + user.getUsername() + " exists! ");
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
                }
            }else{
                log.error("[Services] Username {} have to be more or equle than 4 characters!", user.getUsername());
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }
        }else {
            log.error("[Services] Username can not be null!");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }


        if (user.getPassword() != null) {
            if (!PasswordValidator.isValidPassword(user.getPassword())) {
                log.error("[Services] Password " + user.getPassword() + " is incorect or null! ");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }

            user.setPassword(new BCryptPasswordEncoder(12).encode(user.getPassword()));
        }else{
            log.error("[Services] Password can not be null!");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }


        if (user.getContact() != null){

            if ((user.getContact().getEmail() != null && user.getContact().getPhone() != null)) {

                user.getContact().setContactId(null);

                if (user.getContact().getEmail() != null) {
                    if (contactsRepo.findByEmail(user.getContact().getEmail()) != null) {
                        log.error("[Services] User with this email " + user.getContact().getEmail() + " exists! ");
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
                    }
                }

                if (user.getContact().getPhone() != null) {
                    if (contactsRepo.findByPhone(user.getContact().getPhone()) != null) {
                        log.error("[Services] User with this phone " + user.getContact().getPhone() + " exists! ");
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
                    }
                }

                log.info("[Services] Registering a new contact for user " + user.getUsername() + " .  The contacts are " + user.getContact());
                contactsRepo.save(user.getContact());

            }

        }

        log.info("[Services] Registering a new user " + user);
        return ResponseEntity.status(HttpStatus.OK).body(usersRepo.save(user).getData());
    }

    /**
     * Felhasználói adatok lekérdezése
     * */

    @Override
    public ResponseEntity<Users> getUserDataByUsername(String username) {

        if (usersRepo.findByUsername(username) != null) {
            log.info("[Services] Get User Data By Username" + username);
            return ResponseEntity.status(HttpStatus.OK).body(usersRepo.findByUsername(username).getData());
        }

        log.error("[Controller] User Data for " + username + " not found");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();

    }

    /**
     * Felhasználó frissitése
     * Passwordot nem lehet változtatni!
     * */

    @Override
    public ResponseEntity<Users> updateUserData(Users userIn) {

        if (usersRepo.findById(userIn.getUserId()).isEmpty()){
            log.error("[Services] User with this id " + userIn.getUserId() + " did not exists! ");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        Users user = usersRepo.findById(userIn.getUserId()).get();

        if (userIn.getUsername() != null) {
            if (userIn.getUsername().length() >= 4) {
                if (usersRepo.findByUsername(userIn.getUsername()) != null) {
                    log.error("[Services] User with this username " + userIn.getUsername() + " exists! ");
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
                }
            }else{
                log.error("[Services] Username {} have to be more or equle than 4 characters!", userIn.getUsername());
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }

            user.setUsername(userIn.getUsername());
        }

        if (userIn.getPassword() != null) {
            if (!PasswordValidator.isValidPassword(userIn.getPassword())) {
                log.error("[Services] Password " + user.getPassword() + " is incorect or null! ");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }

            user.setPassword(new BCryptPasswordEncoder(12).encode(userIn.getPassword()));
        }


        if (userIn.getContact() != null) {

            if ((user.getContact().getEmail() != null && user.getContact().getPhone() != null)) {

                if (userIn.getContact().getEmail() != null) {
                    if (contactsRepo.findByEmail(userIn.getContact().getEmail()) != null) {
                        log.error("[Services] User with this email " + userIn.getContact().getEmail() + " exists! ");
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
                    }
                    user.getContact().setEmail(userIn.getContact().getEmail());
                }

                if (userIn.getContact().getPhone() != null) {
                    if (contactsRepo.findByPhone(userIn.getContact().getPhone()) != null) {
                        log.error("[Services] User with this phone " + userIn.getContact().getPhone() + " exists! ");
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
                    }
                    user.getContact().setPhone(userIn.getContact().getPhone());
                }

                log.info("[Services] Registering a new contact for user " + user.getUsername() + " .  The contacts are " + user.getContact());
                contactsRepo.save(user.getContact());
            }
        }

        log.info("[Services] Registering a new user " + user);
        return ResponseEntity.status(HttpStatus.OK).body(usersRepo.save(user).getData());
    }

    /**
     * Felhasználó törlése
     * */

    @Override
    public ResponseEntity<?> deleteUserAccount(Long uid) {

            if (usersRepo.findById(uid).isEmpty()){
                log.info("[Services] User Data for user with id " + uid + " not found");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }

            Users user = usersRepo.findById(uid).get();

            try {

                for (Courses course : user.getUser_appointments()) {
                    course.getStudentsList().remove(user);
                    coursesRepo.save(course);
                }

                // Törli a STUDENT jelentkezéseit
                user.getUser_appointments().clear();
                usersRepo.save(user);

                usersRepo.delete(user);
                log.info("[Services] User deleted!");
            } catch (Exception e) {
                log.error("Error during deletion: ", e);
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }

            return ResponseEntity.status(HttpStatus.OK).build();
    }


    /**
     *  Felhasználói adatok ellenőrzése bejelentkezéshez
     *  Flehasználó név és jelszó megadásával az authentifikációs kezelő eleinte httpBasic mód megpróbálja authentifikálni a felhasználót, amenyiben nincsen JWT Tokene (Ez a rész a SecurityConfig file JWTFilter részénél található). Sikeres authentifikáció esetén generál egy JWT Tokent, amit vissza ad.
     * */

    public String verify(Users user){

        log.info("[Services] Verifying user data: " + user.toString());

        Authentication auth = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        user.getUsername(),
                        user.getPassword()
                )
        );

        if (auth.isAuthenticated()){
            log.info("[Services] Is Authentificated:  " + auth.isAuthenticated());
            log.info("[Services] Principals: " + auth.getPrincipal() + " Username: " + user.getUsername());
            log.info("[Services] Sikeres JWT Token authentifikáció");
            return jwtService.generateToken(user.getUsername());
        }

        log.error("[Services] Sikertelen authentifikáció");
        return "Field to authenticate";

    }

}
