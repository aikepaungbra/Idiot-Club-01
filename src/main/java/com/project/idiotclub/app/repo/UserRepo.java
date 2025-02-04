package com.project.idiotclub.app.repo;

import com.project.idiotclub.app.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepo extends JpaRepository<User, Long> {


    Optional<User> findByEmail(String email);
}
