package com.project.idiotclub.app.repo.member;

import com.project.idiotclub.app.entity.member.CreateClubRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CreateClubRequestRepo extends JpaRepository<CreateClubRequest, Long> {
}
