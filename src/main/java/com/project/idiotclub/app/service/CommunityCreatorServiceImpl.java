package com.project.idiotclub.app.service;

import com.project.idiotclub.app.entity.Community;
import com.project.idiotclub.app.entity.CommunityCreator;
import com.project.idiotclub.app.entity.CommunityInfo;
import com.project.idiotclub.app.repo.CommunityCreatorRepo;
import com.project.idiotclub.app.repo.CommunityInfoRepo;
import com.project.idiotclub.app.repo.CommunityRepo;
import com.project.idiotclub.app.response.ApiResponse;
import com.project.idiotclub.app.response.CommunityCreateResponseDto;
import com.project.idiotclub.app.util.CommunityCreateDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class CommunityCreatorServiceImpl implements CommunityCreatorService {

    @Autowired
    private CommunityCreatorRepo communityCreatorRepo;

    @Autowired
    private CommunityRepo communityRepo;

    @Autowired
    private CommunityInfoRepo communityInfoRepo;

    @Override
    public ApiResponse createCommunity(CommunityCreateDto communityCreateDto) {

        Optional<CommunityCreator> communityCreator = communityCreatorRepo.findById(communityCreateDto.getCommunityCreatorId());

        if(communityCreator.isEmpty()){
            return new ApiResponse(false,"there is no such community creator",null);
        }
        if(communityCreator.get().getCommunity() != null){
            return new ApiResponse(false,"you are already craeted community",null);
        }


        Community community = new Community();
        community.setCommunityName(communityCreateDto.getCommunityName());
        community.setCreateTime(LocalDateTime.now());
        community.setDescription(communityCreateDto.getDescription());
        community.setImage(communityCreateDto.getImage());
        community.setCommunityCreator(communityCreator.get());

        communityRepo.save(community);

        CommunityInfo communityInfo = new CommunityInfo();
        communityInfo.setCommunity(community);
        communityInfo.setClubCount(0);
        communityInfoRepo.save(communityInfo);

        community.setCommunityInfo(communityInfo);
        var com = communityRepo.save(community);

        var responseDto = new CommunityCreateResponseDto();
        responseDto.setCommunityId(com.getCommunityId());
        responseDto.setCommunityName(com.getCommunityName());
        responseDto.setDescription(com.getDescription());
        responseDto.setImage(com.getImage());
        responseDto.setCreateAt(com.getCreateTime());
        responseDto.setCreatorName(com.getCommunityCreator().getCreatorName());
        responseDto.setCreatorEmail(com.getCommunityCreator().getCreatorEmail());
        responseDto.setCommunityInfoId(communityInfo.getId());
        responseDto.setClubCount(communityInfo.getClubCount());


        return new ApiResponse(true,"successfully created",responseDto);

    }

}
