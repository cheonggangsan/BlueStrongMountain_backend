package com.ssafy.BlueStrongMountain.service;

import com.ssafy.BlueStrongMountain.domain.BoardProblem;
import com.ssafy.BlueStrongMountain.domain.BoardUserProgress;
import com.ssafy.BlueStrongMountain.domain.BoardUserStatus;
import com.ssafy.BlueStrongMountain.dto.*;
import com.ssafy.BlueStrongMountain.exception.GroupNotFoundException;
import com.ssafy.BlueStrongMountain.repository.BoardProblemRepository;
import com.ssafy.BlueStrongMountain.repository.BoardUserProgressRepository;
import com.ssafy.BlueStrongMountain.repository.GroupRepository;
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
    private final GroupRepository groupRepository;


    private final BoardUserProgressRepository boardUserProgressRepository;

    /* ===============================
     * Board 생성
     * =============================== */
    @Override
    public Long createBoard(
            Long requesterId,
            Long groupId,
            BoardCreateRequest req) {

        //groupId 검증
        validateGroupExists(groupId);


        if(req.getEndTime() != null && LocalDateTime.now().isAfter(req.getEndTime())){
            throw new RuntimeException("Deadline should not be in the past");
        }

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
        //groupId 검증
        validateGroupExists(groupId);

        BoardDetailResponse findBoard = boardService.getBoard(groupId, boardId);

        if (LocalDateTime.now().isAfter(findBoard.getEndTime())) {
            throw new RuntimeException("Deadline passed. Cannot update.");
        }
        if(req.getEndTime() != null && LocalDateTime.now().isAfter(req.getEndTime())){
            throw new RuntimeException("End time must be after current time");
        }

        Set<Long> beforeProblemIds = new HashSet<>(
                boardProblemRepository.findByBoardId(boardId)
                        .stream()
                        .map(BoardProblem::getProblemId)
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

        boardUserProgressService.updateBoardProgress(
                boardId,
                groupId,
                addedProblemIds.stream().toList(),
                removedProblemIds.stream().toList()
        );
    }

    @Override
    public void deleteBoard(
            Long requesterId,
            Long groupId,
            Long boardId) {
        //groupId 검증
        validateGroupExists(groupId);

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

        Set<Long> solvedProblemIds =
                solvedAcSyncService.getSolvedProblemIds(requesterId);

        for(Long problemId : pendingProblemIds){
            if(solvedProblemIds.contains(problemId)){
                boardUserProgressService.markSolved(
                        boardId,
                        requesterId,
                        problemId
                );
            }
        }
    }

    private void validateGroupExists(Long groupId){
        if(!groupRepository.findById(groupId).isPresent()){
            throw new GroupNotFoundException(groupId);
        }
    }
}
