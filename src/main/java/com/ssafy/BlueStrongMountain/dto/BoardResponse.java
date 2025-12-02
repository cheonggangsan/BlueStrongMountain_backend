package com.ssafy.BlueStrongMountain.dto;

import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@ToString
public class BoardResponse {
    private Long boardId;
    private String title;
    private LocalDateTime endTime;

    public BoardResponse(Long boardId, String title, LocalDateTime endTime) {
        this.boardId = boardId;
        this.title = title;
        this.endTime = endTime;
    }
}