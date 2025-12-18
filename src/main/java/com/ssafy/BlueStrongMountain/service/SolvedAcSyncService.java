package com.ssafy.BlueStrongMountain.service;

import java.util.Set;

public interface SolvedAcSyncService {
    /**
     * solved.ac API를 조회하여
     * user_solution 테이블을 최신 상태로 동기화한다.
     *
     * @param userId 내부 사용자 ID
     */
    void syncUserSolution(Long userId);

    /**
     *  내부 사용자 handle
     */
    boolean existSolvedAcUser(String handle);
    boolean isSolvedAcVerified(String handle, String bio);

    /**
     * user_solution 기준으로 solved problem ids 조회
     */
    Set<Long> getSolvedProblemIds(Long userId);
}
