package com.ssafy.BlueStrongMountain.service;

import com.ssafy.BlueStrongMountain.domain.BoardUserProgress;
import com.ssafy.BlueStrongMountain.domain.BoardUserStatus;
import com.ssafy.BlueStrongMountain.domain.UserGroup;
import com.ssafy.BlueStrongMountain.repository.BoardUserProgressRepository;
import com.ssafy.BlueStrongMountain.repository.UserGroupRepository;
import com.ssafy.BlueStrongMountain.repository.UserSolutionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BoardUserProgressServiceImpl implements BoardUserProgressService {

    private final BoardUserProgressRepository progressRepository;
    private final UserGroupRepository userGroupRepository;

    /* ===============================
     * 초기화
     * =============================== */

    @Override
    public void initializeBoardProgress(
            Long boardId,
            Long groupId,
            List<Long> problemIds
    ) {
        if (problemIds == null || problemIds.isEmpty()) {
            return;
        }

        List<UserGroup> userGroups =
                userGroupRepository.findByGroupId(groupId);

        if (userGroups.isEmpty()) {
            return;
        }

        for (UserGroup userGroup : userGroups) {
            Long userId = userGroup.getUserId();

            for (Long problemId : problemIds) {
                // 이미 있으면 skip
                if (progressRepository.find(boardId, userId, problemId).isPresent()) {
                    continue;
                }

                BoardUserProgress progress =
                        new BoardUserProgress(boardId, userId, problemId);

//                // user_solution에 있으면 solved 처리
//                userSolutionRepository
//                        .findByUserAndProblem(userId, problemId)
//                        .ifPresent(solution -> progress.markSolved());

                progressRepository.save(progress);
            }
        }
    }

    /* ===============================
     * 문제 풀이 처리
     * =============================== */

    @Override
    public void markSolved(
            Long boardId,
            Long userId,
            Long problemId
    ) {
        progressRepository.updateStatus(
                boardId,
                userId,
                problemId,
                BoardUserStatus.SOLVED,
                LocalDateTime.now()
        );
    }

    /* ===============================
     * 조회
     * =============================== */

    @Override
    public List<Long> getSolvedProblemIds(
            Long boardId,
            Long userId
    ) {
        return progressRepository.findProblemIdsByBoardAndUserAndStatus(
                boardId,
                userId,
                BoardUserStatus.SOLVED
        );
    }

    @Override
    public List<Long> getPendingProblemIds(
            Long boardId,
            Long userId
    ) {
        return progressRepository.findProblemIdsByBoardAndUserAndStatus(
                boardId,
                userId,
                BoardUserStatus.PENDING
        );
    }

    @Override
    public List<BoardUserProgress> getProgressByBoardAndUser(
            Long boardId,
            Long userId
    ) {
        return progressRepository.findAllByBoardAndUser(boardId, userId);
    }

    @Override
    public List<BoardUserProgress> getProgressByBoard(Long boardId) {
        return progressRepository.findAllByBoard(boardId);
    }

    /* ===============================
     * 삭제
     * =============================== */
    @Override
    public void deleteByBoard(Long boardId) {
        progressRepository.deleteByBoard(boardId);
    }

    /* ===============================
     * 동기화 (보드 수정)
     * =============================== */

    @Override
    public void updateBoardProgress(
            Long boardId,
            Long groupId,
            List<Long> addedProblemIds,
            List<Long> removedProblemIds
    ) {
        if (addedProblemIds != null && !addedProblemIds.isEmpty()) {
            initializeBoardProgress(boardId, groupId, addedProblemIds);
        }

        if (removedProblemIds != null && !removedProblemIds.isEmpty()) {
            for (Long problemId : removedProblemIds) {
                List<Long> userIds =
                        progressRepository.findUserIdsByBoardAndProblem(boardId, problemId);

                for (Long userId : userIds) {
                    progressRepository.deleteByBoardAndUserAndProblem(
                            boardId,
                            userId,
                            problemId
                    );
                }
            }
        }
    }
}
