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
                .filter(p -> filterByProblemIds(req, p))
                .filter(p -> filterByDifficulty(req, p))
                .filter(p -> filterByTags(req, p))
                .filter(p -> filterByMinSolvers(req, p))
                .toList();
    }
    private boolean filterByProblemIds(ProblemFilterRequest req, ProblemDto p) {
       // System.out.println("filter problem ID here?!!?!??!?!!?!!!!!!!!!!!???????????????????????????????????");//test
        if (req.getProblemIds() == null || req.getProblemIds().isEmpty()) return true;
        //System.out.println(req.getProblemIds().contains(p.getId()));

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

    private boolean filterByMinSolvers(ProblemFilterRequest req, ProblemDto p) {
        if (req.getMinSolvers() == null) return true;
        return p.getAcceptedUserCount() >= req.getMinSolvers();
    }
}
