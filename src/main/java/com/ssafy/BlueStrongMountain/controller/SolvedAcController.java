package com.ssafy.BlueStrongMountain.controller;

import com.ssafy.BlueStrongMountain.service.SolvedAcSyncService;
import com.ssafy.BlueStrongMountain.service.UserSolutionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.Set;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/test/solvedac")
public class SolvedAcController {

    private final SolvedAcSyncService solvedAcSyncService;
    private final UserSolutionService userSolutionService;


    /**
     * solved.ac 동기화 테스트
     *
     * 예:
     * POST /api/test/solvedac/sync?userId=1
     */
    @PostMapping("/sync")
    public ResponseEntity<Void> syncSolvedAc(
            @RequestParam Long userId
    ) {
        solvedAcSyncService.syncUserSolution(userId);
        return ResponseEntity.ok().build();
    }

    /**
     * solved.ac baekjoon handle 확인 테스트
     *
     *
     */
    @GetMapping("/existHandle")
    public ResponseEntity<Boolean> checkBaekHandle(
            @RequestParam String handle
    ){
        return ResponseEntity.ok(solvedAcSyncService.existSolvedAcUser(handle));
    }
    /**
     * solved.ac 인증 여부 확인
     */
    @GetMapping("/verify")
    public ResponseEntity<Boolean> verifySolvedAcAccount(
            @RequestParam String handle,
            @RequestParam String bio
    ) {
        boolean verified = solvedAcSyncService.isSolvedAcVerified(handle, bio);
        return ResponseEntity.ok(verified);
    }


    /**
     * 유저가 푼 문제 ID 목록 조회
     *
     * 예:
     * GET /api/test/solvedac/solved?userId=1
     */
    @GetMapping("/solved")
    public ResponseEntity<Set<Long>> getSolvedProblemIds(
            @RequestParam Long userId
    ) {
        return ResponseEntity.ok(
                new HashSet<>(userSolutionService.findSolvedProblemIds(userId))
        );
    }
}
