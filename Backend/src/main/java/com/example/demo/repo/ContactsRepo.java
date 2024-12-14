package com.example.demo.repo;

import com.example.demo.model.Contacts;
import com.example.demo.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContactsRepo extends JpaRepository<Contacts, Long> {

    Contacts findByEmail(String email);
    Contacts findByPhone(String phone);

}
