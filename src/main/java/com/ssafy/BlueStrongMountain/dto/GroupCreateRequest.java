package com.ssafy.BlueStrongMountain.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class GroupCreateRequest {

    private String title;
    private List<Long> managerIds;
    private List<Long> memberIds;
    private String visibility; // "PRIVATE"
    private String description;
}
