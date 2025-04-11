package com.project.idiotclub.app.repo.community;

import com.project.idiotclub.app.entity.community.Community;
import com.project.idiotclub.app.entity.community.JoinCommunityRequest;
import com.project.idiotclub.app.entity.member.User;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JoinCommunityRequestRepo extends JpaRepository<JoinCommunityRequest, Long> {

    boolean existsByUserAndCommunity(User user, Community community);
    
    List<JoinCommunityRequest> findByCommunityId(Long communityId);
    
}
