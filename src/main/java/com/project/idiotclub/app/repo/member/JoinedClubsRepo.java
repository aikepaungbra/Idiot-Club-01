package com.project.idiotclub.app.repo.member;

import com.project.idiotclub.app.entity.leader.MyClub;
import com.project.idiotclub.app.entity.member.JoinedClubs;
import com.project.idiotclub.app.entity.member.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

public interface JoinedClubsRepo extends JpaRepository<JoinedClubs, Long> {

    JoinedClubs findByUserAndMyClub(User user, MyClub club);

    List<JoinedClubs> findByMyClub(MyClub club);

}
