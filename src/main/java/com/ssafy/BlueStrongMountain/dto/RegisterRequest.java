package com.ssafy.BlueStrongMountain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@ToString
public class RegisterRequest {
    private final String email;
    private final String password;
    private final String username;
    private final String baekjoon;
}
