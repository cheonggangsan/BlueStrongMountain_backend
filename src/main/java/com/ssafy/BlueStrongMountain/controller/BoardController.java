package com.ssafy.BlueStrongMountain.controller;

import com.ssafy.BlueStrongMountain.dto.*;
import com.ssafy.BlueStrongMountain.service.BoardApplicationService;
import com.ssafy.BlueStrongMountain.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/groups/{groupId}/boards")
@RequiredArgsConstructor
public class BoardController {

    private final BoardApplicationService boardApplicationService;
    private final BoardService boardService;

    /**
     * POST /api/v1/groups/{groupId}/boards
     * 보드 생성
     */
    @PostMapping
    public Long createBoard(
            @PathVariable Long groupId,
            @RequestParam Long requesterId,
            @RequestBody BoardCreateRequest request
    ) {
        //return boardService.createBoard(requesterId, groupId, request);
        Long tmp = boardApplicationService.createBoard(requesterId, groupId, request);
//        System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
//        System.out.println(tmp);

        return tmp;
    }

    /**
     * GET /api/v1/groups/{groupId}/boards
     * 목록 조회 (검색 조건 포함)
     */
    @GetMapping
    public List<BoardResponse> getBoards(
            @PathVariable Long groupId,
            @ModelAttribute BoardSearchCondition condition
    ) {
        return boardService.getBoards(groupId, condition);
    }

    /**
     * GET /api/v1/groups/{groupId}/boards/{boardId}
     * 단건 조회
     */
    @GetMapping("/{boardId}")
    public BoardDetailResponse getBoard(
            @PathVariable Long groupId,
            @PathVariable Long boardId
    ) {
        return boardService.getBoard(groupId, boardId);
    }

    /**
     * GET /api/v1/groups/{groupId}/boards/{boardId}/userStatus
     * 유저 풀이 여부 조회
     */
    @GetMapping("/{boardId}/userStatus")
    public BoardProgressResponse getBoardUserStatus(
            @RequestParam Long requesterId,
            @PathVariable Long groupId,
            @PathVariable Long boardId
    ){
        return boardApplicationService.getBoardProgress(requesterId, groupId, boardId);
    }


    /**
     * PATCH /api/v1/groups/{groupId}/boards/{boardId}
     * 보드 수정
     */
    @PatchMapping("/{boardId}")
    public void updateBoard(
            @PathVariable Long groupId,
            @PathVariable Long boardId,
            @RequestParam Long requesterId,
            @RequestBody BoardUpdateRequest request
    ) {
        boardApplicationService.updateBoard(requesterId, groupId, boardId, request);
    }

    /**
     * DELETE /api/v1/groups/{groupId}/boards/{boardId}
     * 보드 삭제
     */
    @DeleteMapping("/{boardId}")
    public void deleteBoard(
            @PathVariable Long groupId,
            @PathVariable Long boardId,
            @RequestParam Long requesterId
    ) {
        boardApplicationService.deleteBoard(requesterId, groupId, boardId);
    }
}
