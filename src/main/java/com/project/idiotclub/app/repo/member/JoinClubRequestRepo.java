package com.project.idiotclub.app.repo.member;

import com.project.idiotclub.app.entity.RequestStatus;
import com.project.idiotclub.app.entity.leader.MyClub;
import com.project.idiotclub.app.entity.member.JoinClubRequest;
import com.project.idiotclub.app.entity.member.User;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

public interface JoinClubRequestRepo extends JpaRepository<JoinClubRequest, Long> {

    List<JoinClubRequest> findByMyClub(MyClub club);

	List<JoinClubRequest> findByMyClubId(Long clubId);

	boolean existsByUserAndMyClubAndRequestStatusIn(User user, MyClub club, List<RequestStatus> of);

	List<JoinClubRequest> findByUserAndMyClub(User user, MyClub club);

	
}
