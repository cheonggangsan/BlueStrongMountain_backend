package com.ssafy.BlueStrongMountain.service;

import com.ssafy.BlueStrongMountain.dto.ProblemDto;
import com.ssafy.BlueStrongMountain.repository.BoardProblemRepository;
import com.ssafy.BlueStrongMountain.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class GroupAnalysisServiceImpl implements GroupAnalysisService{
    private final BoardProblemRepository boardProblemRepository;
    private final ProblemFetchService problemFetchService;
    private final BoardRepository boardRepository;

    @Override
    public Map<String, Long> countSolvedProblemTagsByGroupId(Long groupId) {
        Map<String, Long> tagCountByCategory = new HashMap<>();

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
            Map.entry("union_find", "Graph/DSU"),
            Map.entry("flow", "Graph/Flow"),
            Map.entry("bipartite_matching", "Graph/Matching"),
            Map.entry("scc", "Graph/SCC"),
            Map.entry("2_sat", "Graph/2-SAT"),

            // Data Structure
            Map.entry("data_structures", "DataStructure"),
            Map.entry("stack", "DataStructure"),
            Map.entry("queue", "DataStructure"),
            Map.entry("deque", "DataStructure"),
            Map.entry("priority_queue", "DataStructure"),
            Map.entry("segtree", "DataStructure/SegmentTree"),
            Map.entry("fenwick", "DataStructure/Fenwick"),
            Map.entry("sparse_table", "DataStructure/SparseTable"),
            Map.entry("trie", "DataStructure/Trie"),
            Map.entry("hashing", "DataStructure/Hashing"),

            // Greedy / Implementation / String
            Map.entry("greedy", "Greedy"),
            Map.entry("implementation", "Implementation"),
            Map.entry("string", "String"),
            Map.entry("kmp", "String"),
            Map.entry("suffix_array", "String"),
            Map.entry("lcp", "String"),

            // Math / Geometry
            Map.entry("math", "Math"),
            Map.entry("number_theory", "Math"),
            Map.entry("combinatorics", "Math"),
            Map.entry("geometry", "Geometry"),

            // Search / Paradigms
            Map.entry("binary_search", "Search"),
            Map.entry("parametric_search", "Search"),
            Map.entry("divide_and_conquer", "Divide&Conquer"),
            Map.entry("two_pointer", "TwoPointers"),
            Map.entry("sliding_window", "SlidingWindow"),
            Map.entry("bruteforcing", "Bruteforce"),
            Map.entry("backtracking", "Backtracking"),
            Map.entry("meet_in_the_middle", "MeetInTheMiddle")
    );
}
