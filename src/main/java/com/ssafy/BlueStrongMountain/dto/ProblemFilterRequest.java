package com.ssafy.BlueStrongMountain.dto;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProblemFilterRequest {
    private String mode; // "normal" or "review"
    private List<Integer> problemIds;
    private Integer difficultyFrom;
    private Integer difficultyTo;
    private List<String> tags;
    private Integer minSolvers;
    private Boolean unsolved;
    @Override
    public String toString() {
        return "ProblemFilterRequest{" +
                "mode='" + mode + '\'' +
                ", problemIds=" + problemIds +
                ", difficultyFrom=" + difficultyFrom +
                ", difficultyTo=" + difficultyTo +
                ", tags=" + tags +
                ", minSolvers=" + minSolvers +
                ", unsolved=" + unsolved +
                '}';
    }
}
