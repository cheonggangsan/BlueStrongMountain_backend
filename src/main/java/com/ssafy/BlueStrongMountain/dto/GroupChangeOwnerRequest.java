package com.ssafy.BlueStrongMountain.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class GroupChangeOwnerRequest {
    private Long newOwnerId;
}
