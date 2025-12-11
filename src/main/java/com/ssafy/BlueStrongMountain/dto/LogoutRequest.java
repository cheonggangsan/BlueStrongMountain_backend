package com.ssafy.BlueStrongMountain.dto;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class LogoutRequest {
    private String refreshToken;
}
