//package com.ssafy.BlueStrongMountain.repository;
//
//import com.ssafy.BlueStrongMountain.domain.Board;
//import org.springframework.stereotype.Repository;
//
//import java.util.*;
//import java.util.concurrent.atomic.AtomicLong;
//
////@Repository
//public class InMemoryBoardRepository implements BoardRepository{
//    private final Map<Long, Board> store = new HashMap<>();
//    private final AtomicLong sequence = new AtomicLong(1);
//
//    @Override
//    public Board save(Board board) {
//        Long id = board.getId() == null ? sequence.incrementAndGet() : board.getId();
//        Board toSave = board.getId() == null ? board.assignId(id) : board;
//        store.put(id, toSave);
//        return toSave;
//    }
//
//    @Override
//    public Optional<Board> findById(Long boardId) {
//        return Optional.ofNullable(store.get(boardId));
//    }
//
//    @Override
//    public List<Board> findByGroupId(Long groupId) {
//        List<Board> result = new ArrayList<>();
//        for (Board board : store.values()) {
//            if (board.getGroupId().equals(groupId)) {
//                result.add(board);
//            }
//        }
//        return result;
//    }
//
//    @Override
//    public void delete(Long boardId) {
//        store.remove(boardId);
//    }
//}
