package com.project.idiotclub.app.auth.dto;

import com.project.idiotclub.app.util.community.CommunityCreateResponseDto;

import lombok.Data;

@Data
public class LoginOutputDto{
	
	private Long communityCreatorId;
	private String creatorName;
	private String creatorEmail;
	private String creatorPassword;
	private String creatorPhoto;
	private boolean hasCommunity;
	private CommunityCreateResponseDto communityInfo;
}
