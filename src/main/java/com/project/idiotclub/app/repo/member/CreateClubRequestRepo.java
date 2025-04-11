package com.project.idiotclub.app.repo.member;

import com.project.idiotclub.app.entity.RequestStatus;
import com.project.idiotclub.app.entity.community.Community;
import com.project.idiotclub.app.entity.member.CreateClubRequest;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CreateClubRequestRepo extends JpaRepository<CreateClubRequest, Long> {
	
}
