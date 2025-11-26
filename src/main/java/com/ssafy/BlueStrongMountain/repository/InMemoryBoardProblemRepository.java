package com.ssafy.BlueStrongMountain.repository;

import com.ssafy.BlueStrongMountain.domain.BoardProblem;
import com.ssafy.BlueStrongMountain.repository.BoardProblemRepository;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class InMemoryBoardProblemRepository implements BoardProblemRepository {
    // id 기반 저장소
    private final Map<Long, BoardProblem> store = new HashMap<>();
    // 보드 단위 조회 인덱스
    private final Map<Long, List<Long>> boardIndex = new HashMap<>();
    private final AtomicLong sequence = new AtomicLong(1);
    @Override
    public void deleteByBoardId(Long boardId) {

        List<Long> problemIds = boardIndex.getOrDefault(boardId, new ArrayList<>());

        // id 리스트 삭제
        for (Long id : problemIds) {
            store.remove(id);
        }

        // 인덱스 삭제
        boardIndex.remove(boardId);
    }

    @Override
    public void saveAll(List<BoardProblem> problems) {

        for (BoardProblem p : problems) {
            Long newId = sequence.getAndIncrement();

            // Immutable 객체이므로 assignId로 새 객체 생성
            BoardProblem saved = p.assignId(newId);
            store.put(newId, saved);

            // boardId 인덱스 갱신
            boardIndex.computeIfAbsent(saved.getBoardId(), k -> new ArrayList<>())
                    .add(newId);
        }
    }

    @Override
    public List<BoardProblem> findByBoardId(Long boardId) {

        List<Long> ids = boardIndex.getOrDefault(boardId, Collections.emptyList());

        List<BoardProblem> result = new ArrayList<>();
        for (Long id : ids) {
            BoardProblem bp = store.get(id);
            if (bp != null) {
                result.add(bp);
            }
        }

        return result;
    }
}
