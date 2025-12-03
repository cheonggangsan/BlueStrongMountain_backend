package com.ssafy.BlueStrongMountain.service;

import com.ssafy.BlueStrongMountain.domain.Board;
import com.ssafy.BlueStrongMountain.domain.BoardProblem;
import com.ssafy.BlueStrongMountain.dto.*;
import com.ssafy.BlueStrongMountain.repository.BoardProblemRepository;
import com.ssafy.BlueStrongMountain.repository.BoardRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BoardServiceImpl implements BoardService{
    private final BoardRepository boardRepository;
    private final BoardProblemRepository boardProblemRepository;

    public BoardServiceImpl(BoardRepository boardRepository,
                            BoardProblemRepository boardProblemRepository) {
        this.boardRepository = boardRepository;
        this.boardProblemRepository = boardProblemRepository;
    }
    @Override
    public Long createBoard(Long requesterId, Long groupId, BoardCreateRequest request) {

        Board created = Board.create(
                groupId,
                requesterId,
                request.getTitle(),
                null,                              // content (현재 기능에서는 없음)
                request.getEndTime()
        );

        // Repository에서 ID 부여 (immutable 객체라 새 객체 생성)
        Board saved = boardRepository.save(created);

        // 문제 매핑 저장
        List<BoardProblem> problems = new ArrayList<>();
        int order = 0;
        for (Long pid : request.getProblemIds()) {
            problems.add(BoardProblem.create(saved.getId(), pid, order++));
        }

        boardProblemRepository.saveAll(problems);

        return saved.getId();
    }

    @Override
    public List<BoardResponse> getBoards(Long groupId, BoardSearchCondition condition) {
        List<Board> boards = boardRepository.findByGroupId(groupId);

        // (검색 조건은 이후 QueryDSL 또는 필터 로직 추가)
        return boards.stream()
                .map(b -> new BoardResponse(b.getId(), b.getTitle(), b.getEndTime()))
                .collect(Collectors.toList());
    }
    @Override
    public BoardDetailResponse getBoard(Long groupId, Long boardId) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new RuntimeException("Board not found"));

        List<Long> problemIds = boardProblemRepository.findByBoardId(boardId)
                .stream()
                .map(BoardProblem::getProblemId)
                .toList();
        // (검색 조건은 이후 QueryDSL 또는 필터 로직 추가)
        return new BoardDetailResponse(
                board.getId(),
                board.getTitle(),
                board.getStartTime(),
                board.getEndTime(),
                problemIds
        );
    }

    @Override
    public void updateBoard(Long requesterId, Long groupId, Long boardId, BoardUpdateRequest request) {
        Board currentBoard = boardRepository.findById(boardId)
                .orElseThrow(() -> new RuntimeException("Board not found"));

        if (LocalDateTime.now().isAfter(currentBoard.getEndTime())) {
            throw new RuntimeException("Deadline passed. Cannot update.");
        }

        // Immutable → 수정된 새 객체를 생성
        Board updatedBoard = currentBoard.update(
                request.getTitle(),
                request.getEndTime(),
                null
        );

        boardRepository.save(updatedBoard);

        // 문제 목록 갱신
        boardProblemRepository.deleteByBoardId(boardId);

        List<BoardProblem> newProblems = new ArrayList<>();
        int order = 0;
        for (Long pid : request.getProblemIds()) {
            newProblems.add(BoardProblem.create(boardId, pid, order++));
        }

        boardProblemRepository.saveAll(newProblems);
    }


    @Override
    public void deleteBoard(Long requesterId, Long groupId, Long boardId) {
        boardRepository.delete(boardId);
        boardProblemRepository.deleteByBoardId(boardId);
    }
}
