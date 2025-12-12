package com.ssafy.BlueStrongMountain.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@NoArgsConstructor
public class LogoutRequest {
    @NotBlank
    private String refreshToken;
}
