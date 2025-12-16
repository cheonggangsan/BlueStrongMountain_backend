package com.ssafy.BlueStrongMountain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Getter
@AllArgsConstructor
@ToString
public class ProblemSimpleDto {
    private Long id;
    private String title;
    private Integer difficulty;
}
