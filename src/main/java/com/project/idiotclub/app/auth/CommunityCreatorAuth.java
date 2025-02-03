package com.project.idiotclub.app.auth;

import com.project.idiotclub.app.entity.CommunityCreator;
import com.project.idiotclub.app.repo.CommunityCreatorRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CommunityCreatorAuth {

    @Autowired
    private CommunityCreatorRepo communityCreatorRepo;

    public CommunityCreator signUp(String name,String email,String password) {
        if(communityCreatorRepo.findByCreatorEmail(email).isPresent()){
            return null;
        }
        CommunityCreator communityCreator = new CommunityCreator();
        communityCreator.setCreatorEmail(email);
        communityCreator.setCreatorName(name);
        communityCreator.setCreatorPassword(password);
        communityCreatorRepo.save(communityCreator);
        return communityCreator;
    }

    public boolean login(String email, String password) {
        Optional<CommunityCreator> communityCreator = communityCreatorRepo.findByCreatorEmail(email);
        if(communityCreator.isPresent()) {
            return communityCreator.get().getCreatorPassword().equals(password);
        }
        return false;
    }

}
