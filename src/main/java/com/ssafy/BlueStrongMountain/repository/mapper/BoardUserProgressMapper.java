package com.ssafy.BlueStrongMountain.repository.mapper;

import com.ssafy.BlueStrongMountain.domain.BoardUserProgress;
import com.ssafy.BlueStrongMountain.domain.BoardUserStatus;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Mapper
public interface BoardUserProgressMapper {
    /* ==================================================
     * 단건 조회 / 검증
     * ================================================== */
    Optional<BoardUserProgress> findByBoardUserProblem(
            @Param("boardId") Long boardId,
            @Param("userId") Long userId,
            @Param("problemId") Long problemId
    );

    /* ==================================================
     * INSERT
     * ================================================== */
    int insert(BoardUserProgress progress);

    int insertAll(@Param("progresses")List<BoardUserProgress> progresses);

    /* ==================================================
     * UPDATE
     * ================================================== */
    int updateStatus(
            @Param("boardId") Long boardId,
            @Param("userId") Long userId,
            @Param("problemId") Long problemId,
            @Param("status")BoardUserStatus status,
            @Param("solvedAt")LocalDateTime solvedAt
    );

    /* ==================================================
     * board + user → problem
     * ================================================== */

    List<Long> findProblemIdsByBoardAndUser(
            @Param("boardId") Long boardId,
            @Param("userId") Long userId
    );

    List<Long> findProblemIdsByBoardAndUserAndStatus(
            @Param("boardId") Long boardId,
            @Param("userId") Long userId,
            @Param("status") BoardUserStatus status
    );

    /* ==================================================
     * board + problem → user
     * ================================================== */

    List<Long> findUserIdsByBoardAndProblem(
            @Param("boardId") Long boardId,
            @Param("problemId") Long problemId
    );

    List<Long> findUserIdsByBoardAndProblemAndStatus(
            @Param("boardId") Long boardId,
            @Param("problemId") Long problemId,
            @Param("status") BoardUserStatus status
    );

    /* ==================================================
     * board → user / problem
     * ================================================== */

    List<Long> findUserIdsByBoard(
            @Param("boardId") Long boardId
    );

    List<Long> findUserIdsByBoardAndStatus(
            @Param("boardId") Long boardId,
            @Param("status") BoardUserStatus status
    );

    List<Long> findProblemIdsByBoard(
            @Param("boardId") Long boardId
    );

    /* ==================================================
     * row 단위 조회
     * ================================================== */

    List<BoardUserProgress> findAllByBoardAndUser(
            @Param("boardId") Long boardId,
            @Param("userId") Long userId
    );

    List<BoardUserProgress> findAllByBoard(
            @Param("boardId") Long boardId
    );

    /* ==================================================
     * DELETE
     * ================================================== */

    void deleteByBoard(
            @Param("boardId") Long boardId
    );

    void deleteByBoardAndUser(
            @Param("boardId") Long boardId,
            @Param("userId") Long userId
    );

    void deleteByBoardAndUserAndProblem(
            @Param("boardId") Long boardId,
            @Param("userId") Long userId,
            @Param("problemId") Long problemId);
}
