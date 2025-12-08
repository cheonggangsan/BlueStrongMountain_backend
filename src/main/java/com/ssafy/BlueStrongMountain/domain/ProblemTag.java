package com.ssafy.BlueStrongMountain.domain;

import lombok.Getter;

@Getter
public class ProblemTag {

    private final Long id;
    private final Long problemId;
    private final Long tagId;

    public ProblemTag(Long id, Long problemId, Long tagId) {
        this.id = id;
        this.problemId = problemId;
        this.tagId = tagId;
    }
}
