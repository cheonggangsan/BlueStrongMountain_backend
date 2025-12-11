package com.ssafy.BlueStrongMountain.dto;

import jakarta.annotation.security.DenyAll;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Getter
@AllArgsConstructor
@ToString
public class GroupDetailDto {
    private final Long id;
    private final String title;
    private final String description;
    private final Long ownerId;
    private final List<Long> managers;
    private final List<Long> members;
    private final String visibility;
    private final String createdAt;
    private final String updatedAt;
}
