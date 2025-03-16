package com.project.idiotclub.app.repo.community;

import com.project.idiotclub.app.entity.community.CommunityInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommunityInfoRepo extends JpaRepository<CommunityInfo, Long> {
}
