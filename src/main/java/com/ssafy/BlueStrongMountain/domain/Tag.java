package com.ssafy.BlueStrongMountain.domain;

import lombok.Getter;

@Getter
public class Tag {

    private final Long id;
    private final String name;

    public Tag(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Tag withName(String name) {
        return new Tag(this.id, name);
    }
}
