package com.ssafy.BlueStrongMountain.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@Getter
public class GroupAddIdsRequest {
    private List<Long> userIds;
}
