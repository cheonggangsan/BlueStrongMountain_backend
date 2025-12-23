package com.ssafy.BlueStrongMountain.aiQuery;

import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class BojSemanticSearchService {

    private final GmsOpenAiEmbeddingClient embeddingService;
    private final InMemoryBojEmbeddingIndex index;

    public BojSemanticSearchService(GmsOpenAiEmbeddingClient embeddingService, InMemoryBojEmbeddingIndex index) {
        this.embeddingService = embeddingService;
        this.index = index;
    }

    public List<InMemoryBojEmbeddingIndex.Hit> search(String queryText, int topK) throws Exception {
        if (queryText == null || queryText.isBlank()) {
            throw new IllegalArgumentException("queryText는 비어있을 수 없습니다.");
        }
        if (topK <= 0) topK = 10;

        var qEmb = embeddingService.getQueryEmbedding(queryText);
        return index.searchTopK(qEmb, topK);
    }
}
