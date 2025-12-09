package com.ssafy.BlueStrongMountain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserSolutionResponse {
    private Long userId;
    private Long problemId;
    private String solvedAt;
}
