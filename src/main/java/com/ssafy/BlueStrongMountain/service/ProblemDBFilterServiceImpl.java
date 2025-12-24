package com.ssafy.BlueStrongMountain.service;

import com.ssafy.BlueStrongMountain.aiQuery.BojSemanticSearchService;
import com.ssafy.BlueStrongMountain.aiQuery.InMemoryBojEmbeddingIndex;
import com.ssafy.BlueStrongMountain.domain.UserGroup;
import com.ssafy.BlueStrongMountain.dto.ProblemDto;
import com.ssafy.BlueStrongMountain.dto.ProblemFilterCondition;
import com.ssafy.BlueStrongMountain.exception.SemanticSearchFailedException;
import com.ssafy.BlueStrongMountain.exception.SemanticSearchInsufficientResultException;
import com.ssafy.BlueStrongMountain.exception.SemanticSearchTimeoutException;
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
    private final BojSemanticSearchService bojSemanticSearchService;
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

        if((option == 1 || option == 2)
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
                groupLevel = Math.max(groupLevel, 3);

                int difficultyFrom = Math.max(groupLevel - 2, 1);
                int difficultyTo = Math.min(groupLevel + 2, 35);
                reCondition = reCondition.withDifficultyFrom(difficultyFrom);
                reCondition = reCondition.withDifficultyTo(difficultyTo);
            }
        }
        List<ProblemDto> filterRet = problemRepository.findByCondition(
                groupId,
                unsolved,
                reCondition
        );

        if(option == 1){
            if(filterRet.isEmpty()){
                return List.of();
            }
            List<ProblemDto> shuffledList = new ArrayList<>(filterRet);
            Collections.shuffle(shuffledList);


            return shuffledList.subList(0, Math.min(5, shuffledList.size()));
        }
        if(option == 2){

            List<Long> problemIds =
                    semanticSearchWithRetry(
                            buildSemanticSearchPrompt(reCondition)
                    );
            //return problemRepository.findByIdList(problemIds);
            List<ProblemDto> shuffledList = problemRepository.findByIdList(problemIds);
            Collections.shuffle(shuffledList);

            return shuffledList.subList(0, Math.min(5, shuffledList.size()));
        }
        return filterRet;
    }

    private String buildSemanticSearchPrompt(ProblemFilterCondition cond){
        StringBuilder sb = new StringBuilder();

        sb.append("Problem selection conditions:\n");

        if (cond.getDifficultyFrom() != null || cond.getDifficultyTo() != null) {
            sb.append("- Difficulty range: ")
                    .append(cond.getDifficultyFrom() != null ? cond.getDifficultyFrom() : "any")
                    .append(" to ")
                    .append(cond.getDifficultyTo() != null ? cond.getDifficultyTo() : "any")
                    .append("\n");
        }

        if (cond.getMinSolvers() != null) {
            sb.append("- Minimum number of solvers: ")
                    .append(cond.getMinSolvers())
                    .append("\n");
        }

        if (cond.getTags() != null && !cond.getTags().isEmpty()) {
            sb.append("- Required tags: ")
                    .append(String.join(", ", cond.getTags()))
                    .append("\n");
        }
        return sb.toString();
    }
    private static final int TARGET_SIZE = 25;
    private static final int MAX_ATTEMPTS = 3;
    private static final long TIMEOUT_MS = 1500;

    private List<Long> semanticSearchWithRetry(
            String prompt
    ) {

        long startTime = System.currentTimeMillis();
        int attempt = 0;

        List<Long> result = new ArrayList<>();

        while (attempt < MAX_ATTEMPTS) {
            attempt++;

            // ⏱ 타임아웃 체크
            if (System.currentTimeMillis() - startTime > TIMEOUT_MS) {
                throw new SemanticSearchTimeoutException(
                        "Semantic search timeout after " + attempt + " attempts"
                );
            }

            List<InMemoryBojEmbeddingIndex.Hit> hits;
            try {
                hits = bojSemanticSearchService.search(prompt, TARGET_SIZE);
            } catch (IllegalArgumentException e) {
                // ❌ 프롬프트 자체가 잘못됨 → 재시도 의미 없음
                throw e;
            } catch (Exception e) {
                throw new SemanticSearchFailedException("Semantic search failed", e);
            }

            for (InMemoryBojEmbeddingIndex.Hit hit : hits) {
                long problemId = hit.problemId(); // int → long
                if (!result.contains(problemId)) {
                    result.add(problemId);
                }
            }

            if (result.size() >= TARGET_SIZE) {
                return result.subList(0, TARGET_SIZE);
            }
        }

        throw new SemanticSearchInsufficientResultException(
                "Semantic search returned only " + result.size() + " results"
        );
    }

}
