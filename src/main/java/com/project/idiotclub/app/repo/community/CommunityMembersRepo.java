package com.project.idiotclub.app.repo.community;

import com.project.idiotclub.app.entity.community.Community;
import com.project.idiotclub.app.entity.community.CommunityMembers;
import com.project.idiotclub.app.entity.member.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface CommunityMembersRepo extends JpaRepository<CommunityMembers, Long> {

    boolean existsByUserAndCommunity(User user, Community community);

    @Modifying
    @Transactional
    void deleteByUserAndCommunity(User user, Community community);

    List<CommunityMembers> findByUser(User user);
    
    List<CommunityMembers> findByCommunity_CommunityId(Long communityId);

}

