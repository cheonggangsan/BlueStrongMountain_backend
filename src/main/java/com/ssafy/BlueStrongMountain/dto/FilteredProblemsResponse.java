package com.ssafy.BlueStrongMountain.dto;

import java.util.List;
import lombok.Getter;

@Getter
public class FilteredProblemsResponse {
    private boolean success;
    private List<ProblemDto> data;

    public FilteredProblemsResponse(boolean success, List<ProblemDto> data) {
        this.success = success;
        this.data = data;
    }
    @Override
    public String toString() {
        return "FilteredProblemsResponse{" +
                "success=" + success +
                ", data=" + data +
                '}';
    }
}
