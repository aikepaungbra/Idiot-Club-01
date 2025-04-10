package com.project.idiotclub.app.auth;

import com.project.idiotclub.app.auth.dto.LoginOutputDto;
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
        
        LoginOutputDto loginOutputDto = new LoginOutputDto(
        		communityCreator.getCommunityCreatorId(),
        		communityCreator.getCreatorName(),
        		communityCreator.getCreatorEmail(),
        		communityCreator.getCreatorPassword(),
        		communityCreator.getCreatorPhoto(),
        		communityCreator.getCommunity(),
        		false);

        
        return new ApiResponse(true,"succssfully sign up",loginOutputDto);
    }

    public ApiResponse login(String email, String password) {
        Optional<CommunityCreator> communityCreator = communityCreatorRepo.findByCreatorEmail(email);
        if(communityCreator.isEmpty()) {
            return new ApiResponse(false,"there is no such email",null);
        }
        if(!communityCreator.get().getCreatorPassword().equals(password)){
            return new ApiResponse(false,"incorrect password",null);
        }
        
        boolean hasCommunity = communityCreator.get().getCommunity() != null;
        
        LoginOutputDto loginOutputDto = new LoginOutputDto(
        		communityCreator.get().getCommunityCreatorId(),
        		communityCreator.get().getCreatorName(),
        		communityCreator.get().getCreatorEmail(),
        		communityCreator.get().getCreatorPassword(),
        		communityCreator.get().getCreatorPhoto(),
        		communityCreator.get().getCommunity(),
        		hasCommunity);
        
        return new ApiResponse(true,"successfully login",loginOutputDto);
    }
}
