package com.ssafy.BlueStrongMountain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
@AllArgsConstructor
@ToString
public class BoardProgressResponse {
    private Long boardId;
    private List<BoardProblemStatusDto> problemStatus;
    private List<BoardUserStatusDto> userStatus;
}
