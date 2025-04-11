package com.project.idiotclub.app.util.community;

import lombok.Data;

@Data
public class ViewJoinCommunityRequestOutputForm {
	
	private String userName;
	private String userPhoto;
	private Long userId;
	private Long joinCommunityRequestId;

}
