package com.example.demo.repo;

import com.example.demo.model.Roles;
import com.example.demo.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UsersRepo extends JpaRepository<Users, Long> {

    Users findByUsername(String username);

    List<Users> findByRole(Roles role);

    boolean existsByUsername(String username);

}
