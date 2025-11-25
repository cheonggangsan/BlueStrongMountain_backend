package com.ssafy.BlueStrongMountain.dto;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
public class ProblemFilterRequest {
    private final String mode; // "normal" or "review"
    private final List<Long> problemIds;
    private final Integer difficultyFrom;
    private final Integer difficultyTo;
    private final List<String> tags;
    private final Integer minSolvers;
    private final Boolean unsolved;

    public ProblemFilterRequest(String mode, List<Long> problemIds, Integer difficultyFrom, Integer difficultyTo,
                                List<String> tags, Integer minSolvers, Boolean unsolved) {
        this.mode = (mode != null && !mode.isEmpty()) ? mode : "normal";  // 기본값 처리
        this.problemIds = problemIds != null ? problemIds : Collections.emptyList();
        this.difficultyFrom = difficultyFrom != null ? difficultyFrom : 0;
        this.difficultyTo = difficultyTo != null ? difficultyTo : 35;
        this.tags = tags != null ? tags : Collections.emptyList();
        this.minSolvers = minSolvers != null ? minSolvers : 0;
        this.unsolved = unsolved != null ? unsolved : true;
    }

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
