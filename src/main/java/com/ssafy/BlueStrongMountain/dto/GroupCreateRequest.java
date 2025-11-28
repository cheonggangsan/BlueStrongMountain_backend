package com.ssafy.BlueStrongMountain.dto;

import java.util.List;
import lombok.Getter;

@Getter
public class GroupCreateRequest {

    private String title;
    private List<Long> managerIds;
    private List<Long> memberIds;
    private String visibility; // "PRIVATE"
    private String description;
}
