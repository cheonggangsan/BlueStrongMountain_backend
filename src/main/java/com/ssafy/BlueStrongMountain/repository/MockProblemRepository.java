//package com.ssafy.BlueStrongMountain.repository;
//
//import com.ssafy.BlueStrongMountain.dto.ProblemDto;
//import java.util.List;
//import java.util.Set;
//import org.springframework.stereotype.Repository;
//
////@Repository
//public class MockProblemRepository implements ProblemRepository {
//    private static final List<ProblemDto> MOCK_PROBLEM_DTOS = List.of(
//            new ProblemDto(1409L, "피보나치 수 1", 10, List.of("DP"), 1200, "2024-11-01", 2),
//            new ProblemDto(1508L, "DFS와 BFS", 11, List.of("Graph", "Breadth-first Search(BFS)", "DFS"), 3920, "2024-11-02", 1),
//            new ProblemDto(1000L, "A+B", 0, List.of("implementation"), 99999, "2024-11-03", 0),
//            new ProblemDto(1001L, "A-B", 0, List.of("dp"), 99999, "2024-11-04", 3),
//            new ProblemDto(1002L, "A*B", 0, List.of("dp"), 99999, "2024-11-05", 1),
//            new ProblemDto(1003L, "A/B", 0, List.of("Implementation"), 99999, "2024-11-06", 0),
//            new ProblemDto(1004L, "A%B", 0, List.of("Implementation"), 99999, "2024-11-07", 2),
//            new ProblemDto(1005L, "A&B", 0, List.of("bfs"), 99999, "2024-11-08", 4),
//            new ProblemDto(1006L, "A|B", 0, List.of("Implementation"), 99999, "2024-11-09", 0),
//            new ProblemDto(1007L, "A^B", 0, List.of("Implementation"), 99999, "2024-11-10", 2),
//            new ProblemDto(1008L, "A@B", 0, List.of("Implementation"), 99999, "2024-11-11", 1),
//            new ProblemDto(1009L, "A#B", 0, List.of("Implementation"), 99999, "2024-11-12", 5),
//            new ProblemDto(2000L, "이분 탐색 기본", 7, List.of("Binary Search"), 8000, "2024-11-13", 0),
//            new ProblemDto(2004L, "이것은 세상에서 제일 긴 문제 이름입니다. 테스트를 위해서 이렇게 일단 만들었습니다. 과연 어떻게 나올 것인가", 7, List.of("Binary Search"), 8000, "2024-11-14", 2)
//    );
//    @Override
//    public List<ProblemDto> findAll() {
//        return MOCK_PROBLEM_DTOS;
//    }
//
//    @Override
//    public List<ProblemDto> findByIdList(List<Long> ids) {
//        return MOCK_PROBLEM_DTOS.stream()
//                .filter(p -> ids.contains(p.getId()))
//                .toList();
//    }
//}
