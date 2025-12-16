package com.ssafy.BlueStrongMountain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@ToString
@AllArgsConstructor
public class BoardDetailResponse {

    private Long boardId;
    private String title;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private List<ProblemSimpleDto> problems;
}