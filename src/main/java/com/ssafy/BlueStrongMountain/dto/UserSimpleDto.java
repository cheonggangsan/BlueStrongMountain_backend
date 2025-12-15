package com.ssafy.BlueStrongMountain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@NoArgsConstructor
@ToString
public class UserSimpleDto {
    private Long id;
    private String email;
    private String username;

    public static UserSimpleDto from(Long id, String email, String username){
        UserSimpleDto dto = new UserSimpleDto();
        dto.id = id;
        dto.username = username;
        dto.email = email;
        return dto;
    }
}
