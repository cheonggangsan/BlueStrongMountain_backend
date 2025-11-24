package com.ssafy.BlueStrongMountain.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ProblemDto {
    private Integer id;
    private String title;
    private Integer difficulty;
    private List<String> tags;
    private Integer acceptedUserCount;
    private String registeredAt;
    private Integer reviewCount;
    @Override
    public String toString() {
        return "ProblemDto{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", difficulty=" + difficulty +
                ", tags=" + tags +
                ", acceptedUserCount=" + acceptedUserCount +
                ", registeredAt='" + registeredAt + '\'' +
                ", reviewCount=" + reviewCount +
                '}';
    }
}
