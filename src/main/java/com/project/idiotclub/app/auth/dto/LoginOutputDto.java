package com.project.idiotclub.app.auth.dto;

import lombok.Data;

@Data
public class LoginOutputDto{
	
	private Long communityCreatorId;
	private String creatorName;
	private String creatorEmail;
	private String creatorPassword;
	private String creatorPhoto;
	private Object community;
	private boolean hasCommunity;
}
