package com.ssafy.BlueStrongMountain.aiQuery;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.*;
import java.time.Duration;
import java.util.List;

@Service
public class GmsOpenAiEmbeddingClient {

    private final String openAiApiUrl;
    private final String apiKey;
    private final String embeddingModel;

    private final HttpClient client = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(10))
            .build();
    private final ObjectMapper om = new ObjectMapper();

    public GmsOpenAiEmbeddingClient(
            @Value("${ssafy.gms.openai-api-url}") String openAiApiUrl,
            @Value("${ssafy.gms.api-key}") String apiKey,
            @Value("${spring.ai.openai.embedding.options.model}") String embeddingModel
    ) {
        this.openAiApiUrl = openAiApiUrl;
        this.apiKey = apiKey;
        this.embeddingModel = embeddingModel;
    }

    public record EmbeddingRequest(String model, String input) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record EmbeddingResponse(List<EmbeddingData> data) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record EmbeddingData(List<Double> embedding) {}

    public List<Float> getQueryEmbedding(String queryText) throws Exception {
        if (apiKey == null || apiKey.isBlank()) {
            throw new IllegalStateException("ssafy.gms.api-key가 비어있습니다.");
        }
        if (openAiApiUrl == null || openAiApiUrl.isBlank()) {
            throw new IllegalStateException("ssafy.gms.openai-api-url이 비어있습니다.");
        }
        if (embeddingModel == null || embeddingModel.isBlank()) {
            throw new IllegalStateException("spring.ai.openai.embedding.options.model이 비어있습니다.");
        }

        String body = om.writeValueAsString(new EmbeddingRequest(embeddingModel, queryText));

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(openAiApiUrl))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + apiKey)
                .timeout(Duration.ofSeconds(30))
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            throw new RuntimeException("Embeddings 호출 실패: " + response.statusCode() + "\n" + response.body());
        }

        EmbeddingResponse parsed = om.readValue(response.body(), EmbeddingResponse.class);

        if (parsed == null || parsed.data() == null || parsed.data().isEmpty()) {
            throw new RuntimeException("Embedding 응답이 비어있습니다.");
        }
        if (parsed.data().get(0).embedding() == null) {
            throw new RuntimeException("Embedding 데이터가 null입니다.");
        }

        List<Double> embD = parsed.data().get(0).embedding();

        return embD.stream().map(Double::floatValue).toList();
    }
}
