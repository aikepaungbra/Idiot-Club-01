package com.project.idiotclub.app.repo.community;

import com.project.idiotclub.app.entity.community.Community;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface CommunityRepo extends JpaRepository<Community, Long> {

    List<Community> findByCommunityNameContainingIgnoreCase(String name);

}
