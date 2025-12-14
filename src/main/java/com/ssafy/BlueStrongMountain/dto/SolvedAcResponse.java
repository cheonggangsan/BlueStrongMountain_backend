package com.ssafy.BlueStrongMountain.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Getter
@NoArgsConstructor
@ToString
public class SolvedAcResponse {
    private int count;
    private List<SolvedAcProblem> items;
}
