package com.project.idiotclub.app.util.clubleader;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class RemoveClubMemberForm {

    @NotNull
    private Long leaderId;
    @NotNull
    private Long clubId;
    @NotNull
    private Long memberId;
}
