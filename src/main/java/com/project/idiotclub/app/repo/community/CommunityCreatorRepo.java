package com.project.idiotclub.app.repo.community;

import com.project.idiotclub.app.entity.community.CommunityCreator;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CommunityCreatorRepo extends JpaRepository<CommunityCreator, Long> {
    Optional<CommunityCreator> findByCreatorEmail(String creatorEmail);
}
