package com.ssafy.BlueStrongMountain.service;

import com.ssafy.BlueStrongMountain.domain.Board;
import com.ssafy.BlueStrongMountain.domain.BoardUserProgress;
import com.ssafy.BlueStrongMountain.domain.BoardUserStatus;
import com.ssafy.BlueStrongMountain.dto.*;
import com.ssafy.BlueStrongMountain.repository.BoardProblemRepository;
import com.ssafy.BlueStrongMountain.repository.BoardUserProgressRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
@Transactional
public class BoardApplicationServiceImpl implements BoardApplicationService{
    private final BoardService boardService;
    private final SolvedAcSyncService solvedAcSyncService;
    private final BoardUserProgressService boardUserProgressService;

    private final BoardProblemRepository boardProblemRepository;


    private final BoardUserProgressRepository boardUserProgressRepository;

    /* ===============================
     * Board 생성
     * =============================== */
    @Override
    public Long createBoard(
            Long requesterId,
            Long groupId,
            BoardCreateRequest req) {
        Long boardId
                = boardService.createBoard(
                        requesterId,
                        groupId,
                        req);

        boardUserProgressService.initializeBoardProgress(
                boardId,
                groupId,
                req.getProblemIds()
        );

        return boardId;
    }

    @Override
    public BoardProgressResponse getBoardProgress(
            Long requesterId,
            Long groupId,
            Long boardId) {
        List<BoardUserProgress> progresses =
                boardUserProgressService.getProgressByBoard(boardId);

        Map<Long, List<Long>> problemSolvedMap = new HashMap<>();
        Map<Long, List<Long>> userSolvedMap = new HashMap<>();

        for(BoardUserProgress progress : progresses){
            if(progress.getStatus() != BoardUserStatus.SOLVED) continue;

            Long problemId = progress.getProblemId();
            Long userId = progress.getUserId();

            problemSolvedMap
                    .computeIfAbsent(problemId, k -> new ArrayList<>())
                    .add(userId);
            userSolvedMap
                    .computeIfAbsent(userId, k -> new ArrayList<>())
                    .add(problemId);
        }

        List<BoardProblemStatusDto> problemStatus =
                problemSolvedMap.entrySet()
                    .stream()
                    .map(e -> new BoardProblemStatusDto(e.getKey(), e.getValue()))
                    .toList();
        List<BoardUserStatusDto> userStatus =
                userSolvedMap.entrySet()
                        .stream()
                        .map(e -> new BoardUserStatusDto(e.getKey(), e.getValue()))
                        .toList();

        return new BoardProgressResponse(
                boardId,
                problemStatus,
                userStatus
        );
    }


    /* ===============================
     * Board 수정
     * =============================== */
    @Override
    public void updateBoard(
            Long requesterId,
            Long groupId,
            Long boardId,
            BoardUpdateRequest req) {

        Set<Long> beforeProblemIds = new HashSet<>(
                boardProblemRepository.findByBoardId(boardId)
                        .stream()
                        .map(bp -> bp.getProblemId())
                        .toList()
        );
        Set<Long> afterProblemIds = new HashSet<>(req.getProblemIds());

        Set<Long> addedProblemIds = new HashSet<>(afterProblemIds);
        addedProblemIds.removeAll(beforeProblemIds);

        Set<Long> removedProblemIds = new HashSet<>(beforeProblemIds);
        removedProblemIds.removeAll(afterProblemIds);

        boardService.updateBoard(
                requesterId,
                groupId,
                boardId,
                req
        );

        boardUserProgressService.syncProblems(
                boardId,
                addedProblemIds.stream().toList(),
                removedProblemIds.stream().toList()
        );
    }

    @Override
    public void deleteBoard(
            Long requesterId,
            Long groupId,
            Long boardId) {
        boardUserProgressService.deleteByBoard(boardId);

        boardService.deleteBoard(
                requesterId,
                groupId,
                boardId
        );
    }


    @Override
    public void syncUserProgress(
            Long requesterId,
            Long groupId,
            Long boardId
    ) {
        BoardDetailResponse curBoard = boardService.getBoard(groupId, boardId);
        //데드라인 지났을 경우 sync 종료
        if(curBoard.getEndTime().isBefore(LocalDateTime.now())){
            return;
        }

        solvedAcSyncService.syncUserSolution(requesterId);

        List<Long> pendingProblemIds =
                boardUserProgressService.getPendingProblemIds(boardId, requesterId);

        Set<Long> solved =
                solvedAcSyncService.getSolvedProblemIds(requesterId);

        for(Long problemId : pendingProblemIds){
            boardUserProgressService.markSolved(
                    boardId,
                    requesterId,
                    problemId
            );
        }
    }
}
