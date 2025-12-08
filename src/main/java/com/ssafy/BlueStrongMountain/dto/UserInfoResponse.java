package com.ssafy.BlueStrongMountain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
@ToString
public class UserInfoResponse {
    private final Long userId;
    private final String email;
    private final String username;
    private final String status;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;
}
