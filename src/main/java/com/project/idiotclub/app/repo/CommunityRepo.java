package com.project.idiotclub.app.repo;

import com.project.idiotclub.app.entity.Community;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommunityRepo extends JpaRepository<Community, Long> {
}
