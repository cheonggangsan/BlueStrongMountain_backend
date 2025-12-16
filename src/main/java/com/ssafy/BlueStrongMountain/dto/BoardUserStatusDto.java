package com.ssafy.BlueStrongMountain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
@AllArgsConstructor
@ToString
public class BoardUserStatusDto {
    private final Long userId;
    private final String username;
    private List<Long> solvedProblemIds;
}
