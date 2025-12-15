package com.ssafy.BlueStrongMountain.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@ToString
@NoArgsConstructor
public class BoardUpdateRequest {
    private String title;
    private LocalDateTime endTime;
    private List<Long> problemIds;
}
