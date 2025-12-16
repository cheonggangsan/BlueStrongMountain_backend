package com.ssafy.BlueStrongMountain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class GroupUpdateRequest {

    private String title;
    private List<Long> managerIds;
    private List<Long> memberIds;
    private String visibility;
    private String description;
}
