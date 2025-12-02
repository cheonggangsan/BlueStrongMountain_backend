package com.ssafy.BlueStrongMountain.dto;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class BoardSearchCondition {
    private String title;
    private String status; // ONGOING, FINISHED
    private Integer page;
    private Integer size;
    private String sort;

}
