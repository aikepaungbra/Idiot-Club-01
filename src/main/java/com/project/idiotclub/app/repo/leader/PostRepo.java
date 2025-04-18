package com.project.idiotclub.app.repo.leader;

import com.project.idiotclub.app.entity.leader.MyClub;
import com.project.idiotclub.app.entity.leader.Post;
import com.project.idiotclub.app.entity.member.User;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepo extends JpaRepository<Post, Long> {

    List<Post> findByMyClubOrderByCreatedAtDesc(MyClub club);

	List<Post> findByUserAndMyClub(User user, MyClub club);
	
}

