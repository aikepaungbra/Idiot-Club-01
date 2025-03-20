package com.project.idiotclub.app.repo.leader;

import com.project.idiotclub.app.entity.community.Community;
import com.project.idiotclub.app.entity.leader.MyClub;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

public interface MyClubRepo extends JpaRepository<MyClub, Long> {

    List<MyClub> findByCommunityAndNameContainingIgnoreCase(Community community, String clubName);

    List<MyClub> findByCommunity(Community community);

}
