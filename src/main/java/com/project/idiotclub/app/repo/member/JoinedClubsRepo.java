package com.project.idiotclub.app.repo.member;

import com.project.idiotclub.app.entity.community.Community;
import com.project.idiotclub.app.entity.leader.MyClub;
import com.project.idiotclub.app.entity.member.JoinedClubs;
import com.project.idiotclub.app.entity.member.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface JoinedClubsRepo extends JpaRepository<JoinedClubs, Long> {

    JoinedClubs findByUserAndMyClub(User user, MyClub club);

    List<JoinedClubs> findByMyClub(MyClub club);

    boolean existsByUserAndMyClub(User user, MyClub myClub);

    List<JoinedClubs> findByUserAndMyClub_Community(User user, Community community);

    int countByMyClub(MyClub club);

}
