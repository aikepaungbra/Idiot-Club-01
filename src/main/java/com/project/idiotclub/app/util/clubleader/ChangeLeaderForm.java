package com.project.idiotclub.app.util.clubleader;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ChangeLeaderForm {

    @NotNull
    private Long currentLeaderId;
    @NotNull
    private Long newLeaderId;
    @NotNull
    private Long clubId;

}
