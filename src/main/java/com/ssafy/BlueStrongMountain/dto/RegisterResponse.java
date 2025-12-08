package com.ssafy.BlueStrongMountain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@ToString
public class RegisterResponse {
    private final Long userId;
    private final String email;
    private final String username;
    private final String status;
}
