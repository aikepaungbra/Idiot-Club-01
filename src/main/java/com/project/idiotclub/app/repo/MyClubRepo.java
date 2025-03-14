package com.project.idiotclub.app.repo;

import com.project.idiotclub.app.entity.MyClub;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MyClubRepo extends JpaRepository<MyClub, Long> {
}
