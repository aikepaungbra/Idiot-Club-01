package com.project.idiotclub.app.repo.community;

import com.project.idiotclub.app.entity.RequestStatus;
import com.project.idiotclub.app.entity.community.Community;
import com.project.idiotclub.app.entity.community.JoinCommunityRequest;
import com.project.idiotclub.app.entity.member.User;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JoinCommunityRequestRepo extends JpaRepository<JoinCommunityRequest, Long> {

    boolean existsByUserAndCommunity(User user, Community community);
    
//    List<JoinCommunityRequest> findByCommunity(Community community);
    
    List<JoinCommunityRequest> findByCommunityAndStatus(Community community, RequestStatus status);

	List<JoinCommunityRequest> findByUserAndCommunity(User user, Community community);
}
