package com.ssafy.BlueStrongMountain.service;

import com.ssafy.BlueStrongMountain.domain.UserGroup;
import com.ssafy.BlueStrongMountain.dto.ProblemDto;
import com.ssafy.BlueStrongMountain.dto.ProblemFilterCondition;
import com.ssafy.BlueStrongMountain.repository.ProblemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProblemDBFilterServiceImpl implements ProblemDBFilterService{
    private final ProblemRepository problemRepository;

    private final GroupAnalysisService groupAnalysisService;


    @Override
    public List<ProblemDto> applyFilterFindByIds(List<Long> problemIds) {
        return problemRepository.findByIdList(problemIds);
    }

    @Override
    public List<ProblemDto> applyFilter(
            Long groupId,
            boolean unsolved,
            Integer option,
            ProblemFilterCondition problemFilterCondition) {

        ProblemFilterCondition reCondition = problemFilterCondition;

        //FIXME random 기능에도 추천 기능 적용되고 있음
        if((option == 2 || option == 1)
                && problemFilterCondition.getTags().isEmpty()){
            List<String> recommendTags = groupAnalysisService.recommendTagsByGroup(groupId);

            reCondition = problemFilterCondition.withTags(recommendTags);

            if(reCondition.getMinSolvers() == 0){
                reCondition = reCondition.withMinSolvers(100);
            }
            //그룹에서의 해결 문제 평균으로 탐색
            if(reCondition.getDifficultyFrom() == 1 &&
                    reCondition.getDifficultyTo() == 35){
                int groupLevel = (int) groupAnalysisService.calculateAverageDifficultyByGroupId(groupId);
                groupLevel = Math.max(groupLevel, 1);

                int difficultyFrom = Math.max(groupLevel - 1, 1);
                int difficultyTo = Math.min(groupLevel + 1, 35);
                reCondition = reCondition.withDifficultyFrom(difficultyFrom);
                reCondition = reCondition.withDifficultyTo(difficultyTo);
            }
        }
        List<ProblemDto> filterRet = problemRepository.findByCondition(
                groupId,
                unsolved,
                reCondition
        );

        //FIXME random 기능에도 추천 기능 적용되고 있음
        if(option == 2 || option == 1){
            if(filterRet.isEmpty()){
                return List.of();
            }
            List<ProblemDto> shuffledList = new ArrayList<>(filterRet);
            Collections.shuffle(shuffledList);


            return shuffledList.subList(0, Math.min(5, shuffledList.size()));
        }
        return filterRet;
    }
}
