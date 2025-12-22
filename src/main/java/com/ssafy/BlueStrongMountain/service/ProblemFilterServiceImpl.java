package com.ssafy.BlueStrongMountain.service;

import com.ssafy.BlueStrongMountain.dto.ProblemDto;
import com.ssafy.BlueStrongMountain.dto.ProblemFilterRequest;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProblemFilterServiceImpl implements ProblemFilterService{

    private final GroupAnalysisService groupAnalysisService;


    @Override
    public List<ProblemDto> applyFilters(Long groupId, List<ProblemDto> base, ProblemFilterRequest req) {

        ProblemFilterRequest effectiveReq = req;


        //FIXME random 기능에도 추천 기능 적용되고 있음
        if((req.getOption() == 2 || req.getOption() == 1) && req.getTags().isEmpty()){
            //group 분석해서 추천 태그 설정
            List<String> recommendTags = groupAnalysisService.recommendTagsByGroup(groupId);

            effectiveReq = req.withTags(recommendTags);
            //최소 해결자 100명 이상
            if(effectiveReq.getMinSolvers() == 0)
                effectiveReq = effectiveReq.withMinSolvers(100);
            //그룹에서의 해결 문제 평균으로 탐색
            if(effectiveReq.getDifficultyFrom() == 0 &&
                    effectiveReq.getDifficultyTo() == 35){
                int groupLevel = (int) groupAnalysisService.calculateAverageDifficultyByGroupId(groupId);
                int difficultyFrom = Math.max(groupLevel - 1, 0);
                int difficultyTo = Math.min(groupLevel + 1, 35);
                effectiveReq = effectiveReq.withDifficulty(difficultyFrom, difficultyTo);
            }
        }
        ProblemFilterRequest finalReq = effectiveReq;

        List<ProblemDto> filterRet = base.stream()
                .filter(p -> filterByProblemIds(finalReq, p))
                .filter(p -> filterByDifficulty(finalReq, p))
                .filter(p -> filterByTags(finalReq, p))
                .filter(p -> filterByMinSolvers(finalReq, p))
                .toList();

        //FIXME random 기능에도 추천 기능 적용되고 있음
        if(req.getOption() == 1 || req.getOption() == 2){
            if(filterRet.isEmpty()){
                return List.of();
            }
            List<ProblemDto> shuffledList = new ArrayList<>(filterRet);
            Collections.shuffle(shuffledList);


            return shuffledList.subList(0, Math.min(5, shuffledList.size()));
        }
        return filterRet;
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
