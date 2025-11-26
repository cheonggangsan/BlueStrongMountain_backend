package com.ssafy.BlueStrongMountain.controller;

import com.ssafy.BlueStrongMountain.dto.BoardCreateRequest;
import com.ssafy.BlueStrongMountain.dto.BoardSearchCondition;
import com.ssafy.BlueStrongMountain.dto.BoardUpdateRequest;
import com.ssafy.BlueStrongMountain.dto.BoardDetailResponse;
import com.ssafy.BlueStrongMountain.dto.BoardResponse;
import com.ssafy.BlueStrongMountain.service.BoardService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/groups/{groupId}/boards")
public class BoardController {

    private final BoardService boardService;

    public BoardController(BoardService boardService) {
        this.boardService = boardService;
    }

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
        Long tmp = boardService.createBoard(requesterId, groupId, request);
        System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        System.out.println(tmp);//test

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
        boardService.updateBoard(requesterId, groupId, boardId, request);
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
        boardService.deleteBoard(requesterId, groupId, boardId);
    }
}
