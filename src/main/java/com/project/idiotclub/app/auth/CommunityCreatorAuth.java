package com.project.idiotclub.app.auth;

import com.project.idiotclub.app.auth.dto.LoginOutputDto;
import com.project.idiotclub.app.entity.community.Community;
import com.project.idiotclub.app.entity.community.CommunityCreator;
import com.project.idiotclub.app.entity.community.CommunityInfo;
import com.project.idiotclub.app.repo.community.CommunityCreatorRepo;
import com.project.idiotclub.app.response.ApiResponse;
import com.project.idiotclub.app.util.community.CommunityCreateResponseDto;

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
        
        LoginOutputDto loginOutputDto = new LoginOutputDto();
        loginOutputDto.setCommunityCreatorId(communityCreator.getCommunityCreatorId());
        loginOutputDto.setCreatorName(communityCreator.getCreatorName());
        loginOutputDto.setCreatorEmail(communityCreator.getCreatorEmail());
        loginOutputDto.setCreatorPassword(communityCreator.getCreatorPassword());
        loginOutputDto.setCreatorPhoto(communityCreator.getCreatorPhoto());
        loginOutputDto.setHasCommunity(false);
        
        
        return new ApiResponse(true,"succssfully sign up",loginOutputDto);
    }

    
    public ApiResponse login(String email, String password) {
        Optional<CommunityCreator> communityCreator = communityCreatorRepo.findByCreatorEmail(email);
        if(communityCreator.isEmpty()) {
            return new ApiResponse(false,"there is no such email",null);
        }
        
        CommunityCreator creator = communityCreator.get();
        
        if(!creator.getCreatorPassword().equals(password)){
            return new ApiResponse(false,"incorrect password",null);
        }
       
        
        LoginOutputDto loginOutputDto = new LoginOutputDto();
        loginOutputDto.setCommunityCreatorId(creator.getCommunityCreatorId());
        loginOutputDto.setCreatorName(creator.getCreatorName());
        loginOutputDto.setCreatorEmail(creator.getCreatorEmail());
        loginOutputDto.setCreatorPassword(creator.getCreatorPassword());
        loginOutputDto.setCreatorPhoto(creator.getCreatorPhoto());
        
//        loginOutputDto.setCommunity(creator.getCommunity());
        
        boolean hasCommunity = creator.getCommunity() != null;
        loginOutputDto.setHasCommunity(hasCommunity);
        
        if(hasCommunity) {
        	Community community = creator.getCommunity();
        	CommunityInfo communityInfo = community.getCommunityInfo();
        	
        	CommunityCreateResponseDto communityDto = new CommunityCreateResponseDto();
        	
        	communityDto.setCommunityId(community.getCommunityId());
        	communityDto.setCommunityName(community.getCommunityName());
        	communityDto.setDescription(community.getDescription());
             communityDto.setImage(community.getImage());
             communityDto.setCreateAt(community.getCreateTime());
             communityDto.setCreatorName(creator.getCreatorName());
             communityDto.setCreatorEmail(creator.getCreatorEmail());
             communityDto.setCommunityInfoId(communityInfo.getId());
             communityDto.setClubCount(communityInfo.getClubCount());
             
             loginOutputDto.setCommunityInfo(communityDto);
        }
        
        return new ApiResponse(true,"successfully login",loginOutputDto);
    }
}
