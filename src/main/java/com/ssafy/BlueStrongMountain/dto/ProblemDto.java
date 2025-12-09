package com.ssafy.BlueStrongMountain.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@Getter
@ToString
public class ProblemDto {
    private Long id;
    private String title;
    private Integer difficulty;
    private List<String> tags;
    private Integer acceptedUserCount;
    private String updatedAt;
}
