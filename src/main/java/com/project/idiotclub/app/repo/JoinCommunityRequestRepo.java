package com.project.idiotclub.app.repo;

import com.project.idiotclub.app.entity.Community;
import com.project.idiotclub.app.entity.JoinCommunityRequest;
import com.project.idiotclub.app.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JoinCommunityRequestRepo extends JpaRepository<JoinCommunityRequest, Long> {

    boolean existsByUserAndCommunity(User user, Community community);
}
