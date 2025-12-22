package com.ssafy.BlueStrongMountain.dto;


import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
public class ProblemFilterCondition {

    private final Integer difficultyFrom;
    private final Integer difficultyTo;
    private final Integer minSolvers;
    private final List<String> tags;

        /* =========================
       with-methods (불변 수정)
       ========================= */

    public ProblemFilterCondition withDifficultyFrom(Integer difficultyFrom) {
        return new ProblemFilterCondition(
                difficultyFrom,
                this.difficultyTo,
                this.minSolvers,
                this.tags
        );
    }

    public ProblemFilterCondition withDifficultyTo(Integer difficultyTo) {
        return new ProblemFilterCondition(
                this.difficultyFrom,
                difficultyTo,
                this.minSolvers,
                this.tags
        );
    }

    public ProblemFilterCondition withMinSolvers(Integer minSolvers) {
        return new ProblemFilterCondition(
                this.difficultyFrom,
                this.difficultyTo,
                minSolvers,
                this.tags
        );
    }

    public ProblemFilterCondition withTags(List<String> tags) {
        return new ProblemFilterCondition(
                this.difficultyFrom,
                this.difficultyTo,
                this.minSolvers,
                tags
        );
    }
}

