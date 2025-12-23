package com.ssafy.BlueStrongMountain.aiQuery;

import org.springframework.web.bind.annotation.*;

import java.util.List;

// TOOD: add validation, response DTO, api rate limiting
@RestController
public class BojSearchController {

    private final BojSemanticSearchService bojQueryService;

    public BojSearchController(BojSemanticSearchService bojQueryService) {
        this.bojQueryService = bojQueryService;
    }

    @GetMapping("/api/v1/problem/ai")
    public List<InMemoryBojEmbeddingIndex.Hit> query(
            @RequestParam String queryText,
            @RequestParam(defaultValue = "10") int topK
    ) throws Exception {
        return bojQueryService.search(queryText, topK);
    }
}
