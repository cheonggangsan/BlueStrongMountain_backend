package com.ssafy.BlueStrongMountain.domain;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class Problem {
    private Integer id;
    private String title;
    private Integer difficulty;
    private List<String> tags;
    private Integer acceptedUserCount;
    private String registeredAt;
    private Integer reviewCount;
}
