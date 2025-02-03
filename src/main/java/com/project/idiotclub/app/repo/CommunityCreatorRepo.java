package com.project.idiotclub.app.repo;

import com.project.idiotclub.app.entity.CommunityCreator;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CommunityCreatorRepo extends JpaRepository<CommunityCreator, Long> {
    Optional<CommunityCreator> findByCreatorEmail(String creatorEmail);
}
