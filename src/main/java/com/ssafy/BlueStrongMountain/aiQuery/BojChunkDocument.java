package com.ssafy.BlueStrongMountain.aiQuery;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public class BojChunkDocument {
    private String id;
    private String text;
    private List<Float> embedding;

    private Map<String, Object> metadata;
    private Map<String, Object> problem;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getText() { return text; }
    public void setText(String text) { this.text = text; }

    public List<Float> getEmbedding() { return embedding; }
    public void setEmbedding(List<Float> embedding) { this.embedding = embedding; }

    public Map<String, Object> getMetadata() { return metadata; }
    public void setMetadata(Map<String, Object> metadata) { this.metadata = metadata; }

    public Map<String, Object> getProblem() { return problem; }
    public void setProblem(Map<String, Object> problem) { this.problem = problem; }

    public Integer extractProblemId() {
        Integer pid = tryGetInt(metadata, "problem_id");
        if (pid != null) return pid;

        pid = tryGetInt(problem, "problemId");
        if (pid != null) return pid;

        if (id != null && id.startsWith("boj:")) {
            String[] parts = id.split(":");
            if (parts.length >= 2) {
                try { return Integer.parseInt(parts[1]); } catch (Exception ignored) {}
            }
        }
        return null;
    }

    private Integer tryGetInt(Map<String, Object> map, String key) {
        if (map == null) return null;
        Object v = map.get(key);
        if (v instanceof Integer i) return i;
        if (v instanceof Number n) return n.intValue();
        if (v instanceof String s) {
            try { return Integer.parseInt(s); } catch (Exception ignored) {}
        }
        return null;
    }
}
