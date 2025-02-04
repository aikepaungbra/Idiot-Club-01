package com.project.idiotclub.app.repo;

import com.project.idiotclub.app.entity.Community;
import com.project.idiotclub.app.entity.CommunityMembers;
import com.project.idiotclub.app.entity.User;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CommunityMembersRepo extends JpaRepository<CommunityMembers, Long> {


    boolean existsByUserAndCommunity(User user, Community community);
}
