package com.ssafy.BlueStrongMountain.service;

import com.ssafy.BlueStrongMountain.domain.*;
import com.ssafy.BlueStrongMountain.dto.*;
import com.ssafy.BlueStrongMountain.exception.GroupNotFoundException;
import com.ssafy.BlueStrongMountain.exception.UserNotFoundException;
import com.ssafy.BlueStrongMountain.repository.*;
import com.ssafy.BlueStrongMountain.service.validator.GroupAuthorityService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class BoardApplicationServiceImpl implements BoardApplicationService{
    private final BoardService boardService;
    private final SolvedAcSyncService solvedAcSyncService;
    private final BoardUserProgressService boardUserProgressService;

    private final BoardProblemRepository boardProblemRepository;
    private final GroupRepository groupRepository;
    private final UserGroupRepository userGroupRepository;

    private final UserRepository userRepository;
    private final UserSolutionRepository userSolutionRepository;

    private final GroupAuthorityService groupAuthorityService;


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
        groupAuthorityService.validateManager(requesterId, groupId);


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

        //userSolution sync
        syncBoardUserProgress(requesterId, groupId, boardId);
        //userSolution sync end

        return boardId;
    }

    @Override
    public BoardProgressResponse getBoardProgress(
            Long requesterId,
            Long groupId,
            Long boardId) {

        List<BoardUserProgress> progresses =
                boardUserProgressService.getProgressByBoard(boardId);

        //TODO 임시 동기화 기능으로 생각해야 함
        //userSolution sync
        syncBoardUserProgress(requesterId, groupId, boardId);
        //userSolution sync end

        Map<Long, List<Long>> problemSolvedMap = new HashMap<>();
        Map<Long, List<Long>> userSolvedMap = new HashMap<>();

        Set<Long> allUserIds = new HashSet<>();

        for(BoardUserProgress progress : progresses){
            Long problemId = progress.getProblemId();
            Long userId = progress.getUserId();
            allUserIds.add(userId);

            if(progress.getStatus() != BoardUserStatus.SOLVED){
                continue;
            }


            problemSolvedMap
                    .computeIfAbsent(problemId, k -> new ArrayList<>())
                    .add(userId);
            userSolvedMap
                    .computeIfAbsent(userId, k -> new ArrayList<>())
                    .add(problemId);
        }

        //username 캐싱
        Map<Long, String> usernameMap = new HashMap<>();
        //for(Long userId : userSolvedMap.keySet()){
        for(Long userId : allUserIds){
            String username = userRepository.findById(userId)
                    .orElseThrow(UserNotFoundException::new)
                    .getUsername();
            usernameMap.put(userId, username);
        }

        List<BoardProblemStatusDto> problemStatus =
                problemSolvedMap.entrySet()
                    .stream()
                    .map(e -> new BoardProblemStatusDto(e.getKey(), e.getValue()))
                    .toList();

        List<BoardUserStatusDto> userStatus = new ArrayList<>();
        for(Long userId : allUserIds){

            String username = usernameMap.get(userId);
            List<Long> solvedProblemIds = new ArrayList<>();
            if(userSolvedMap.get(userId) != null && !userSolvedMap.get(userId).isEmpty()){
                solvedProblemIds = userSolvedMap.get(userId);
            }
            userStatus.add(new BoardUserStatusDto(
                    userId,
                    username,
                    solvedProblemIds
            ));
        }


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
        groupAuthorityService.validateManager(requesterId, groupId);

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
        //userSolution sync
        syncBoardUserProgress(requesterId, groupId, boardId);
        //userSolution sync end
    }

    @Override
    public void deleteBoard(
            Long requesterId,
            Long groupId,
            Long boardId) {
        //groupId 검증
        validateGroupExists(groupId);
        groupAuthorityService.validateManager(requesterId, groupId);

        boardUserProgressService.deleteByBoard(boardId);

        boardService.deleteBoard(
                requesterId,
                groupId,
                boardId
        );
    }


    @Override
    public void syncBoardUserProgress(
            Long requesterId,
            Long groupId,
            Long boardId
    ) {
        BoardDetailResponse curBoard = boardService.getBoard(groupId, boardId);
        //데드라인 지났을 경우 sync 종료
        if(curBoard.getEndTime().isBefore(LocalDateTime.now())){
            return;
        }

        List<Long> userIdsInGroup =
                userGroupRepository.findByGroupId(groupId)
                        .stream()
                        .map(UserGroup::getUserId)
                        .toList();
        for(Long userId : userIdsInGroup){
            syncSingleUserProgress(userId, boardId);
        }
    }

    private void syncSingleUserProgress(
            Long userId,
            Long boardId
    ){
//        long startTime = System.nanoTime();

//        solvedAcSyncService.syncUserSolution(requesterId);
//        List<Long> pendingProblemIds =
//                boardUserProgressService.getPendingProblemIds(boardId, requesterId);

        List<Long> pendingProblemIds =
                boardUserProgressService.getPendingProblemIds(boardId, userId);

        Set<Long> solvedProblemIds =
                userSolutionRepository.findByUserId(userId)
                .stream()
                .map(UserSolution::getProblemId)
                .collect(Collectors.toSet());

        for(Long problemId : pendingProblemIds){
            if(solvedProblemIds.contains(problemId)){
                boardUserProgressService.markSolved(
                        boardId,
                        userId,
                        problemId
                );
            }
        }

//        long endTime = System.nanoTime();
//        long elapsedMs = (endTime - startTime) / 1_000_000;
//        System.out.println("syncUserProgress time = " + elapsedMs + "ms");
    }

    private void validateGroupExists(Long groupId){
        if(!groupRepository.findById(groupId).isPresent()){
            throw new GroupNotFoundException(groupId);
        }
    }
}
