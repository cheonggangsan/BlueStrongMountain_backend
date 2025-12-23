package com.ssafy.BlueStrongMountain.aiQuery;

import com.fasterxml.jackson.core.*;
        import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import java.io.File;
import java.util.*;

@Component
public class InMemoryBojEmbeddingIndex {

    public record Entry(int problemId, float[] vec) {}
    public record Hit(int problemId, double score) {}

    private final ObjectMapper om = new ObjectMapper();
    private final File jsonFile;

    private List<Entry> entries = List.of();

    public InMemoryBojEmbeddingIndex(@Value("${boj.embedding.json}") String embeddingFileLocation) {
        this.jsonFile = new File(embeddingFileLocation);
    }

    @PostConstruct
    public void loadOnce() throws Exception {
        List<Entry> loaded = new ArrayList<>();

        JsonFactory factory = om.getFactory();
        try (JsonParser p = factory.createParser(jsonFile)) {

            if (p.nextToken() != JsonToken.START_ARRAY) {
                throw new IllegalArgumentException("JSON이 배열([ ... ]) 형태가 아닙니다.");
            }

            while (p.nextToken() == JsonToken.START_OBJECT) {
                BojChunkDocument chunk = om.readValue(p, BojChunkDocument.class);

                Integer pid = chunk.extractProblemId();
                var emb = chunk.getEmbedding();
                if (pid == null || emb == null || emb.isEmpty()) continue;

                float[] v = toUnitFloatArray(emb);
                loaded.add(new Entry(pid, v));
            }
        }

        this.entries = Collections.unmodifiableList(loaded);
        System.out.println("Loaded vectors: " + this.entries.size());
    }

    public List<Hit> searchTopK(List<Float> queryEmbedding, int topK) {
        float[] q = toUnitFloatArray(queryEmbedding);

        PriorityQueue<Hit> heap = new PriorityQueue<>(Comparator.comparingDouble(Hit::score));

        for (Entry e : entries) {
            double score = dot(q, e.vec);
            if (heap.size() < topK) heap.add(new Hit(e.problemId, score));
            else if (score > heap.peek().score) { heap.poll(); heap.add(new Hit(e.problemId, score)); }
        }

        List<Hit> hits = new ArrayList<>(heap);
        hits.sort((a, b) -> Double.compare(b.score, a.score));
        return hits;
    }

    private float[] toUnitFloatArray(List<Float> list) {
        float[] v = new float[list.size()];
        double normSq = 0;
        for (int i = 0; i < list.size(); i++) {
            float x = list.get(i);
            v[i] = x;
            normSq += (double) x * x;
        }
        double norm = Math.sqrt(normSq);
        if (norm == 0) return v;
        for (int i = 0; i < v.length; i++) v[i] = (float) (v[i] / norm);
        return v;
    }

    private double dot(float[] a, float[] b) {
        if (a.length != b.length) {
            System.out.println("Vector dimension mismatch: " + a.length + " vs " + b.length);
        }
        int n = Math.min(a.length, b.length);
        double s = 0;
        for (int i = 0; i < n; i++) s += (double) a[i] * b[i];
        return s;
    }
}
