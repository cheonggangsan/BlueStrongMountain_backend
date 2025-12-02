package com.ssafy.BlueStrongMountain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BaseResponse {
    private final boolean success;
    public static BaseResponse ok(){
        return new BaseResponse(true);
    }
}
