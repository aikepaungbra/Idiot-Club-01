package com.project.idiotclub.app.auth;

import com.project.idiotclub.app.entity.community.CommunityCreator;
import com.project.idiotclub.app.repo.community.CommunityCreatorRepo;
import com.project.idiotclub.app.response.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CommunityCreatorAuth {

    @Autowired
    private CommunityCreatorRepo communityCreatorRepo;

    public ApiResponse signUp(String name, String email, String password) {
        if(communityCreatorRepo.findByCreatorEmail(email).isPresent()){
            return new ApiResponse(false,"already exist this email",null);
        }
        CommunityCreator communityCreator = new CommunityCreator();
        communityCreator.setCreatorEmail(email);
        communityCreator.setCreatorName(name);
        communityCreator.setCreatorPassword(password);
        communityCreatorRepo.save(communityCreator);
        return new ApiResponse(true,"succssfully sign up",communityCreator);
    }

    public ApiResponse login(String email, String password) {
        Optional<CommunityCreator> communityCreator = communityCreatorRepo.findByCreatorEmail(email);
        if(communityCreator.isEmpty()) {
            return new ApiResponse(false,"there is no such email",null);
        }
        if(!communityCreator.get().getCreatorPassword().equals(password)){
            return new ApiResponse(false,"incorrect password",null);
        }
        return new ApiResponse(true,"successfully login",communityCreator.get());
    }
}
