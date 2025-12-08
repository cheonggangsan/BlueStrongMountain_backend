package com.ssafy.BlueStrongMountain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class UsernameDuplicateResponse {
    private final String username;
    private final boolean duplicated;
}
