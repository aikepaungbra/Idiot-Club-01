package com.project.idiotclub.app.repo.member;

import com.project.idiotclub.app.entity.member.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepo extends JpaRepository<User, Long> {


    Optional<User> findByEmail(String email);

	
}
