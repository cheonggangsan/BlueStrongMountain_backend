package com.ssafy.BlueStrongMountain.dto;

import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@ToString
public class BoardCreateRequest {
    private String title;
    private LocalDateTime endTime;
    private List<Long> problemIds;
}