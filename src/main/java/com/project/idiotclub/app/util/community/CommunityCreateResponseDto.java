package com.project.idiotclub.app.util.community;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CommunityCreateResponseDto {

    private Long communityId;
    private String communityName;
    private String description;
    private String image;
    private LocalDateTime createAt;
    private String creatorName;
    private String creatorEmail;
    private Long communityInfoId;
    private Integer clubCount;

}
