package com.ssafy.BlueStrongMountain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PasswordVerifyRequest {
    private Long userId;
    private String password;
}
