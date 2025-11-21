package com.ssafy.BlueStrongMountain.repository;


import com.ssafy.BlueStrongMountain.domain.Problem;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
public class ProblemRepository {

    // MOCK_PROBLEMS 데이터를 로컬에 저장 (실제 DB 연결 대신)
    private static final List<Problem> MOCK_PROBLEMS = List.of(
            new Problem(1409, "피보나치 수 1", 10, List.of("DP"), 1200, "2024-11-01", 2),
            new Problem(1508, "DFS와 BFS", 11, List.of("Graph", "Breadth-first Search(BFS)", "DFS"), 3920, "2024-11-02", 1),
            new Problem(1000, "A+B", 0, List.of("Implementation"), 99999, "2024-11-03", 0),
            new Problem(1001, "A-B", 0, List.of("Implementation"), 99999, "2024-11-04", 3),
            new Problem(1002, "A*B", 0, List.of("Implementation"), 99999, "2024-11-05", 1),
            new Problem(1003, "A/B", 0, List.of("Implementation"), 99999, "2024-11-06", 0),
            new Problem(1004, "A%B", 0, List.of("Implementation"), 99999, "2024-11-07", 2),
            new Problem(1005, "A&B", 0, List.of("Implementation"), 99999, "2024-11-08", 4),
            new Problem(1006, "A|B", 0, List.of("Implementation"), 99999, "2024-11-09", 0),
            new Problem(1007, "A^B", 0, List.of("Implementation"), 99999, "2024-11-10", 2),
            new Problem(1008, "A@B", 0, List.of("Implementation"), 99999, "2024-11-11", 1),
            new Problem(1009, "A#B", 0, List.of("Implementation"), 99999, "2024-11-12", 5),
            new Problem(2000, "이분 탐색 기본", 7, List.of("Binary Search"), 8000, "2024-11-13", 0),
            new Problem(2004, "이것은 세상에서 제일 긴 문제 이름입니다. 테스트를 위해서 이렇게 일단 만들었습니다. 과연 어떻게 나올 것인가", 7, List.of("Binary Search"), 8000, "2024-11-14", 2)
    );

    // 필터링된 문제 리스트 반환
    public List<Problem> findFilteredProblems(List<Long> problemIds, Integer difficultyFrom, Integer difficultyTo,
                                              List<String> tags, Integer minSolvers, Boolean unsolved) {

        return MOCK_PROBLEMS.stream()
                .filter(problem -> (problemIds == null || problemIds.contains(problem.getId())))
                .filter(problem -> (difficultyFrom == null || (problem.getDifficulty()) >= difficultyFrom))
                .filter(problem -> (difficultyTo == null || (problem.getDifficulty()) <= difficultyTo))
                .filter(problem -> (tags == null || tags.isEmpty() || problem.getTags().stream().anyMatch(tags::contains)))
                .filter(problem -> (minSolvers == null || problem.getAcceptedUserCount() >= minSolvers))
                .filter(problem -> (unsolved == null || (unsolved ? problem.getReviewCount() == 0 : problem.getReviewCount() > 0)))
                .collect(Collectors.toList());
    }

    // 난이도 수준을 숫자로 변환 (예: "Gold 5" → 5)
    private int getDifficultyLevel(String difficulty) {
        switch (difficulty) {
            case "Bronze 5": return 0;
            case "Bronze 4": return 1;
            case "Bronze 3": return 2;
            case "Bronze 2": return 3;
            case "Bronze 1": return 4;

            case "Silver 5": return 5;
            case "Silver 4": return 6;
            case "Silver 3": return 7;
            case "Silver 2": return 8;
            case "Silver 1": return 9;

            case "Gold 5": return 10;
            case "Gold 4": return 11;
            case "Gold 3": return 12;
            case "Gold 2": return 13;
            case "Gold 1": return 14;

            case "Platinum": return 15;
            case "Diamond": return 20;
            case "Ruby": return 25;
            case "Master": return 30;

            case "Unrated": return 35;  // Unrated 처리

            default: return -1; // 기본값 처리
        }
    }
}
