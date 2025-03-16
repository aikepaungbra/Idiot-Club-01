package com.project.idiotclub.app.repo.leader;

import com.project.idiotclub.app.entity.leader.MyClub;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MyClubRepo extends JpaRepository<MyClub, Long> {
}
