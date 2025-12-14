package com.ssafy.BlueStrongMountain.repository;

import com.ssafy.BlueStrongMountain.domain.BoardUserProgress;
import com.ssafy.BlueStrongMountain.domain.BoardUserStatus;
import com.ssafy.BlueStrongMountain.repository.mapper.BoardUserProgressMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class BoardUserProgressRepositoryImpl
        implements BoardUserProgressRepository {

    private final BoardUserProgressMapper mapper;

    /* ===============================
     * 단건 조회 / 검증
     * =============================== */

    @Override
    public Optional<BoardUserProgress> find(
            Long boardId,
            Long userId,
            Long problemId
    ) {
        return mapper.findByBoardUserProblem(boardId, userId, problemId);
    }

    /* ===============================
     * INSERT
     * =============================== */

    @Override
    public void save(BoardUserProgress progress) {
        mapper.insert(progress);
    }

    @Override
    public void saveAll(List<BoardUserProgress> progresses) {
        if (progresses == null || progresses.isEmpty()) {
            return;
        }
        mapper.insertAll(progresses);
    }

    /* ===============================
     * UPDATE
     * =============================== */

    @Override
    public void updateStatus(
            Long boardId,
            Long userId,
            Long problemId,
            BoardUserStatus status,
            LocalDateTime solvedAt
    ) {
        mapper.updateStatus(
                boardId,
                userId,
                problemId,
                status,
                solvedAt
        );
    }

    /* ===============================
     * board + user → problem
     * =============================== */

    @Override
    public List<Long> findProblemIdsByBoardAndUser(
            Long boardId,
            Long userId
    ) {
        return mapper.findProblemIdsByBoardAndUser(boardId, userId);
    }

    @Override
    public List<Long> findProblemIdsByBoardAndUserAndStatus(
            Long boardId,
            Long userId,
            BoardUserStatus status
    ) {
        return mapper.findProblemIdsByBoardAndUserAndStatus(
                boardId, userId, status
        );
    }

    /* ===============================
     * board + problem → user
     * =============================== */

    @Override
    public List<Long> findUserIdsByBoardAndProblem(
            Long boardId,
            Long problemId
    ) {
        return mapper.findUserIdsByBoardAndProblem(boardId, problemId);
    }

    @Override
    public List<Long> findUserIdsByBoardAndProblemAndStatus(
            Long boardId,
            Long problemId,
            BoardUserStatus status
    ) {
        return mapper.findUserIdsByBoardAndProblemAndStatus(
                boardId, problemId, status
        );
    }

    /* ===============================
     * board → user / problem
     * =============================== */

    @Override
    public List<Long> findUserIdsByBoard(Long boardId) {
        return mapper.findUserIdsByBoard(boardId);
    }

    @Override
    public List<Long> findUserIdsByBoardAndStatus(
            Long boardId,
            BoardUserStatus status
    ) {
        return mapper.findUserIdsByBoardAndStatus(boardId, status);
    }

    @Override
    public List<Long> findProblemIdsByBoard(Long boardId) {
        return mapper.findProblemIdsByBoard(boardId);
    }

    /* ===============================
     * row 단위 조회
     * =============================== */

    @Override
    public List<BoardUserProgress> findAllByBoardAndUser(
            Long boardId,
            Long userId
    ) {
        return mapper.findAllByBoardAndUser(boardId, userId);
    }

    @Override
    public List<BoardUserProgress> findAllByBoard(Long boardId) {
        return mapper.findAllByBoard(boardId);
    }

    /* ===============================
     * DELETE
     * =============================== */

    @Override
    public void deleteByBoard(Long boardId) {
        mapper.deleteByBoard(boardId);
    }

    @Override
    public void deleteByBoardAndUser(Long boardId, Long userId) {
        mapper.deleteByBoardAndUser(boardId, userId);
    }

    @Override
    public void deleteByBoardAndUserAndProblem(Long boardId, Long userId, Long problemId) {
        mapper.deleteByBoardAndUserAndProblem(
                boardId,
                userId,
                problemId
        );
    }
}
