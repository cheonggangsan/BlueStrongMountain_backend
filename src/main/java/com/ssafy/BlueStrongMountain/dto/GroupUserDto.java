package com.ssafy.BlueStrongMountain.dto;

import com.ssafy.BlueStrongMountain.domain.GroupRole;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class GroupUserDto {
    private Long userId;
    private String username;
    private GroupRole role;
}
