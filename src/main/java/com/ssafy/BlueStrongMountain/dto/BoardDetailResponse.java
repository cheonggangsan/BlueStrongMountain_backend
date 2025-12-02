package com.ssafy.BlueStrongMountain.dto;

import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@ToString
public class BoardDetailResponse {

    private Long boardId;
    private String title;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private List<Long> problemIds;

    public BoardDetailResponse(Long boardId, String title, LocalDateTime startTime,
                               LocalDateTime endTime, List<Long> problemIds) {
        this.boardId = boardId;
        this.title = title;
        this.startTime = startTime;
        this.endTime = endTime;
        this.problemIds = problemIds;
    }
}