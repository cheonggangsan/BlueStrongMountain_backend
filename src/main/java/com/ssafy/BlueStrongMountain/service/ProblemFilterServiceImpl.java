package com.ssafy.BlueStrongMountain.service;

import com.ssafy.BlueStrongMountain.dto.ProblemDto;
import com.ssafy.BlueStrongMountain.dto.ProblemFilterRequest;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class ProblemFilterServiceImpl implements ProblemFilterService{


    @Override
    public List<ProblemDto> applyFilters(List<ProblemDto> base, ProblemFilterRequest req) {
        return base.stream()
                .filter(p -> filterByIds(req, p))
                .filter(p -> filterByDifficulty(req, p))
                .filter(p -> filterByTags(req, p))
                .filter(p -> filterBySolvers(req, p))
                .toList();
    }
    private boolean filterByIds(ProblemFilterRequest req, ProblemDto p) {
        if (req.getProblemIds() == null || req.getProblemIds().isEmpty()) return true;
        return req.getProblemIds().contains(p.getId());
    }

    private boolean filterByDifficulty(ProblemFilterRequest req, ProblemDto p) {
        if (req.getDifficultyFrom() == null || req.getDifficultyTo() == null) return true;
        return p.getDifficulty() >= req.getDifficultyFrom()
                && p.getDifficulty() <= req.getDifficultyTo();
    }

    private boolean filterByTags(ProblemFilterRequest req, ProblemDto p) {
        if (req.getTags() == null || req.getTags().isEmpty()) return true;
        return p.getTags().stream().anyMatch(tag ->
                req.getTags().contains(tag.toLowerCase())
        );
    }

    private boolean filterBySolvers(ProblemFilterRequest req, ProblemDto p) {
        if (req.getMinSolvers() == null) return true;
        return p.getAcceptedUserCount() >= req.getMinSolvers();
    }
}
