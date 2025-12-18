package com.ssafy.BlueStrongMountain.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Getter
@NoArgsConstructor
@ToString
public class SolvedAcUserSearchResponse {
    private int count;
    private List<SolvedAcUserItemResponse> items;
}
