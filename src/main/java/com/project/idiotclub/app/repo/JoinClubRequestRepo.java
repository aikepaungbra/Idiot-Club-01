package com.project.idiotclub.app.repo;

import com.project.idiotclub.app.entity.JoinClubRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JoinClubRequestRepo extends JpaRepository<JoinClubRequest, Long> {
}
