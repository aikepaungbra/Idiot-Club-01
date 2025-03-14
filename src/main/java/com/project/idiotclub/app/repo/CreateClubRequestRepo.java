package com.project.idiotclub.app.repo;

import com.project.idiotclub.app.entity.CreateClubRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CreateClubRequestRepo extends JpaRepository<CreateClubRequest, Long> {
}
