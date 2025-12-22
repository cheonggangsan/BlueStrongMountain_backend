package com.ssafy.BlueStrongMountain.service;

import com.ssafy.BlueStrongMountain.dto.ProblemDto;
import com.ssafy.BlueStrongMountain.repository.BoardProblemRepository;
import com.ssafy.BlueStrongMountain.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GroupAnalysisServiceImpl implements GroupAnalysisService{
    private final BoardProblemRepository boardProblemRepository;
    private final ProblemFetchService problemFetchService;
    private final BoardRepository boardRepository;

    @Override
    public List<String> recommendTagsByGroup(Long groupId){
        // 1. 카테고리별 풀이 수 집계
        Map<String, Long> tagCountByCategory =
                countSolvedProblemTagsByGroupId(groupId);

        // 2. softmax 확률로 카테고리 하나 선택
        String selectedCategory =
                selectCategoryBySoftmax(tagCountByCategory);

        // 3. 선택된 카테고리에 해당하는 태그 목록 반환
        return mapCategoryToTag(selectedCategory);
    }

    private String selectCategoryBySoftmax(Map<String, Long> tagCountByCategory){
        double beta = 0.5;
        Map<String, Double> weightMap = new HashMap<>();
        double totalWeight = 0.0;

        for(Map.Entry<String, Long> entry : tagCountByCategory.entrySet()){
            long count = entry.getValue();
            double weight = Math.exp(-beta * count);
            weightMap.put(entry.getKey(), weight);
            totalWeight += weight;
        }

        double r = Math.random() * totalWeight;
        double acc = 0.0;

        for(Map.Entry<String, Double> entry : weightMap.entrySet()){
            acc += entry.getValue();
            if(r <= acc){
                return entry.getKey();
            }
        }
        //이론상 도달하진 않음
        return weightMap.keySet().iterator().next();
    }

    @Override
    public double calculateAverageDifficultyByGroupId(Long groupId) {
        List<ProblemDto> problems = fetchProblemsByGroupId(groupId);

        return problems.stream()
                .map(ProblemDto::getDifficulty)
                .filter(Objects::nonNull)
                .mapToInt(Integer::intValue)
                .average()
                .orElse(0.0);
    }

    @Override
    public Map<String, Long> countSolvedProblemTagsByGroupId(Long groupId) {
        Map<String, Long> tagCountByCategory = new HashMap<>();
        tagCountByCategory.put("DP", 0L);
        tagCountByCategory.put("Graph", 0L);
        tagCountByCategory.put("DataStructure", 0L);
        tagCountByCategory.put("Greedy", 0L);
        tagCountByCategory.put("String", 0L);
        tagCountByCategory.put("Math", 0L);
        tagCountByCategory.put("Search", 0L);
        tagCountByCategory.put("BruteForce", 0L);
        tagCountByCategory.put("Implementation", 0L);


        List<ProblemDto> problems = fetchProblemsByGroupId(groupId);

        for(ProblemDto problem : problems){
            List<String> tags = problem.getTags();
            for(String tag : tags){
                String categorizedTag = mapTagToCategory(tag);
                tagCountByCategory.put(
                        categorizedTag,
                        tagCountByCategory.getOrDefault(categorizedTag, 0L) + 1
                );
            }
        }

        //TODO erase this
        for(String key : tagCountByCategory.keySet()){
            System.out.println(key + " " + tagCountByCategory.get(key));
        }



        return tagCountByCategory;
    }


    @Override
    public List<ProblemDto> fetchProblemsByGroupId(Long groupId) {
        return problemFetchService.fetchReviewProblems(groupId);
    }

    private String mapTagToCategory(String tag){
        if (tag == null || tag.isBlank()) {
            return "Unknown";
        }

        return TAG_TO_CATEGORY.getOrDefault(tag, "Other");
    }
    private List<String> mapCategoryToTag(String category) {
        if (category == null || category.isBlank()) {
            return List.of();
        }
        return CATEGORY_TO_TAGS.getOrDefault(category, List.of());
    }
    private static final Map<String, String> TAG_TO_CATEGORY = Map.ofEntries(
            // DP
            Map.entry("dp", "DP"),
            Map.entry("dp_tree", "DP"),
            Map.entry("knapsack", "DP"),
            Map.entry("bitmask_dp", "DP"),
            Map.entry("digit_dp", "DP"),

            // Graph
            Map.entry("graphs", "Graph"),
            Map.entry("graph_traversal", "Graph"),
            Map.entry("bfs", "Graph"),
            Map.entry("dfs", "Graph"),
            Map.entry("shortest_path", "Graph"),
            Map.entry("dijkstra", "Graph"),
            Map.entry("bellman_ford", "Graph"),
            Map.entry("floyd_warshall", "Graph"),
            Map.entry("topological_sorting", "Graph"),
            Map.entry("minimum_spanning_tree", "Graph"),
            Map.entry("union_find", "Graph"),
            Map.entry("flow", "Graph"),
            Map.entry("bipartite_matching", "Graph"),
            Map.entry("scc", "Graph"),
            Map.entry("2_sat", "Graph"),

            // Data Structure
            Map.entry("data_structures", "DataStructure"),
            Map.entry("stack", "DataStructure"),
            Map.entry("queue", "DataStructure"),
            Map.entry("deque", "DataStructure"),
            Map.entry("priority_queue", "DataStructure"),
            Map.entry("segtree", "DataStructure"),
            Map.entry("fenwick", "DataStructure"),
            Map.entry("sparse_table", "DataStructure"),
            Map.entry("trie", "DataStructure"),
            Map.entry("hashing", "DataStructure"),

            // Greedy
            Map.entry("greedy", "Greedy"),

            // Implementation
            Map.entry("implementation", "Implementation"),

            // String
            Map.entry("string", "String"),
            Map.entry("kmp", "String"),
            Map.entry("suffix_array", "String"),
            Map.entry("lcp", "String"),

            // Math / Geometry
            Map.entry("math", "Math"),
            Map.entry("number_theory", "Math"),
            Map.entry("combinatorics", "Math"),
            Map.entry("geometry", "Math"),

            // Search
            Map.entry("binary_search", "Search"),
            Map.entry("parametric_search", "Search"),
            Map.entry("divide_and_conquer", "Search"),
            Map.entry("two_pointer", "Search"),
            Map.entry("sliding_window", "Search"),
            Map.entry("meet_in_the_middle", "Search"),
            Map.entry("backtracking", "Search"),


            // Bruteforce
            Map.entry("bruteforcing", "Bruteforce")
    );

    // 역매핑: category -> tags
    private static final Map<String, List<String>> CATEGORY_TO_TAGS =
            TAG_TO_CATEGORY.entrySet().stream()
                    .collect(Collectors.groupingBy(
                            Map.Entry::getValue,
                            Collectors.mapping(Map.Entry::getKey, Collectors.toUnmodifiableList())
                    ));
}
