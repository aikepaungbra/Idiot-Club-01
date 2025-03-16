package com.project.idiotclub.app.repo.member;

import com.project.idiotclub.app.entity.member.JoinedClubs;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JoinedClubsRepo extends JpaRepository<JoinedClubs, Long> {
}
