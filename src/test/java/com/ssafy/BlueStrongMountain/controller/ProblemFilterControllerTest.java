package com.ssafy.BlueStrongMountain.controller;

import static org.junit.jupiter.api.Assertions.*;
import com.ssafy.BlueStrongMountain.dto.ProblemDto;
import com.ssafy.BlueStrongMountain.dto.FilteredProblemsResponse;
import com.ssafy.BlueStrongMountain.dto.ProblemFilterRequest;
import com.ssafy.BlueStrongMountain.service.ProblemFetchService;
import com.ssafy.BlueStrongMountain.service.ProblemFilterService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
public class ProblemFilterControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ProblemFetchService fetchService;

    @Mock
    private ProblemFilterService filterService;

    @InjectMocks
    private ProblemFilterController problemFilterController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(problemFilterController).build();
    }

    @Test
    void testFilterWithModeNormal() throws Exception {
        // Given
        ProblemFilterRequest request = new ProblemFilterRequest(
                "normal",                    // mode
                new ArrayList<>(),            // problemIds
                0,                            // difficultyFrom
                20,                           // difficultyTo
                new ArrayList<>(),            // tags
                null,                         // minSolvers (null로 전달할 수 있음)
                false                         // unsolved
        );
        List<ProblemDto> mockProblems = new ArrayList<>();
        mockProblems.add(new ProblemDto(1000L, "Problem 1", 10, new ArrayList<>(), 100, "2024-11-01", 5));

        when(fetchService.fetchBaseProblems(any(Long.class), any(Boolean.class))).thenReturn(mockProblems);
        when(filterService.applyFilters(any(List.class), any(ProblemFilterRequest.class))).thenReturn(mockProblems);

        // When & Then
        mockMvc.perform(post("/api/v1/groups/1/problems/filter")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                "  \"mode\": \"normal\",\n" +
                                "  \"problemIds\": [],\n" +
                                "  \"difficultyFrom\": 0,\n" +
                                "  \"difficultyTo\": 20,\n" +
                                "  \"tags\": [],\n" +
                                "  \"minSolvers\": null,\n" +
                                "  \"unsolved\": false\n" +
                                "}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].id").value(1000))
                .andExpect(jsonPath("$.data[0].title").value("Problem 1"))
                .andExpect(jsonPath("$.data[0].difficulty").value(10));
    }

    @Test
    void testFilterWithModeReview() throws Exception {
        // Given
        ProblemFilterRequest request = new ProblemFilterRequest(
                "review",                    // mode
                new ArrayList<>(),            // problemIds
                0,                            // difficultyFrom
                30,                           // difficultyTo
                new ArrayList<>(),            // tags
                null,                         // minSolvers (null로 전달할 수 있음)
                false                         // unsolved
        );
        List<ProblemDto> mockProblems = new ArrayList<>();
        mockProblems.add(new ProblemDto(1500L, "Problem 2", 15, new ArrayList<>(), 200, "2024-11-02", 10));

        when(fetchService.fetchReviewProblems(any(Long.class))).thenReturn(mockProblems);
        when(filterService.applyFilters(any(List.class), any(ProblemFilterRequest.class))).thenReturn(mockProblems);

        // When & Then
        mockMvc.perform(post("/api/v1/groups/1/problems/filter")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                "  \"mode\": \"review\",\n" +
                                "  \"problemIds\": [],\n" +
                                "  \"difficultyFrom\": 0,\n" +
                                "  \"difficultyTo\": 30,\n" +
                                "  \"tags\": [],\n" +
                                "  \"minSolvers\": null,\n" +
                                "  \"unsolved\": false\n" +
                                "}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].id").value(1500))
                .andExpect(jsonPath("$.data[0].title").value("Problem 2"))
                .andExpect(jsonPath("$.data[0].difficulty").value(15));
    }

    @Test
    void testFilterWithEmptyRequest() throws Exception {
        // Given
        ProblemFilterRequest request = new ProblemFilterRequest(
                "normal",                    // mode
                new ArrayList<>(),            // problemIds
                0,                            // difficultyFrom
                20,                           // difficultyTo
                new ArrayList<>(),            // tags
                null,                         // minSolvers (null로 전달할 수 있음)
                false                         // unsolved
        );
        List<ProblemDto> mockProblems = new ArrayList<>();
        when(fetchService.fetchBaseProblems(any(Long.class), any(Boolean.class))).thenReturn(mockProblems);
        when(filterService.applyFilters(any(List.class), any(ProblemFilterRequest.class))).thenReturn(mockProblems);

        // When & Then
        mockMvc.perform(post("/api/v1/groups/1/problems/filter")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                "  \"mode\": \"normal\",\n" +
                                "  \"problemIds\": [],\n" +
                                "  \"difficultyFrom\": 0,\n" +
                                "  \"difficultyTo\": 20,\n" +
                                "  \"tags\": [],\n" +
                                "  \"minSolvers\": null,\n" +
                                "  \"unsolved\": false\n" +
                                "}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isEmpty());
    }
}
