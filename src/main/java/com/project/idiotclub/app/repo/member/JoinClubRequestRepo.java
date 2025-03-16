package com.project.idiotclub.app.repo.member;

import com.project.idiotclub.app.entity.member.JoinClubRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JoinClubRequestRepo extends JpaRepository<JoinClubRequest, Long> {
}
