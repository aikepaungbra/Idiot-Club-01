package com.project.idiotclub.app.repo;

import com.project.idiotclub.app.entity.JoinedClubs;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JoinedClubsRepo extends JpaRepository<JoinedClubs, Long> {
}
