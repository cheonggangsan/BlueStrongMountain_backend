package com.ssafy.BlueStrongMountain.controller;

import com.ssafy.BlueStrongMountain.domain.UserSolution;
import com.ssafy.BlueStrongMountain.dto.UserSolutionCreateRequest;
import com.ssafy.BlueStrongMountain.dto.UserSolutionResponse;
import com.ssafy.BlueStrongMountain.service.UserSolutionService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/user-solutions")
@RequiredArgsConstructor
public class UserSolutionController {

    private final UserSolutionService userSolutionService;

    /** 문제 해결 기록 저장 */
    @PostMapping
    public ResponseEntity<UserSolutionResponse> save(@RequestBody UserSolutionCreateRequest req) {

        UserSolution solution = UserSolution.create(
                req.getUserId(),
                req.getProblemId(),
                LocalDateTime.now()
        );

        userSolutionService.save(solution);

        return ResponseEntity.ok(
                new UserSolutionResponse(
                        solution.getUserId(),
                        solution.getProblemId(),
                        solution.getSolvedAt().toString()
                )
        );
    }

    /** 특정 유저가 푼 문제 목록 조회 */
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Long>> findByUser(@PathVariable Long userId) {
        List<Long> solvedIds = userSolutionService.findSolvedProblemIds(userId);
        return ResponseEntity.ok(solvedIds);
    }

    /** 특정 문제를 푼 유저 목록 조회 */
    @GetMapping("/problem/{problemId}")
    public ResponseEntity<List<UserSolutionResponse>> findByProblem(@PathVariable Long problemId) {
        List<UserSolution> list = userSolutionService.findByProblemId(problemId);

        List<UserSolutionResponse> result = list.stream()
                .map(s -> new UserSolutionResponse(
                        s.getUserId(),
                        s.getProblemId(),
                        s.getSolvedAt().toString()
                ))
                .collect(Collectors.toList());

        return ResponseEntity.ok(result);
    }

    /** 특정 유저가 특정 문제를 풀었는지 여부 조회 */
    @GetMapping("/check")
    public ResponseEntity<Boolean> hasSolved(
            @RequestParam Long userId,
            @RequestParam Long problemId
    ) {
        return ResponseEntity.ok(userSolutionService.hasSolved(userId, problemId));
    }
}
