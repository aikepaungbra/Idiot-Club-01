package com.project.idiotclub.app.repo.member;

import com.project.idiotclub.app.entity.leader.MyClub;
import com.project.idiotclub.app.entity.member.JoinClubRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

public interface JoinClubRequestRepo extends JpaRepository<JoinClubRequest, Long> {

    List<JoinClubRequest> findByMyClub(MyClub club);

	List<JoinClubRequest> findByMyClubId(Long clubId);

	
}
