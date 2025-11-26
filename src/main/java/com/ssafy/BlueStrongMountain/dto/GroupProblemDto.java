package com.ssafy.BlueStrongMountain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GroupProblemDto {
    private Long groupId;
    private Long problemId;
    private int reviewCount;
}
