package com.ssafy.BlueStrongMountain.domain;

import lombok.Getter;
import java.time.LocalDateTime;


@Getter
public final class Board {

    private final Long id;
    private final Long groupId;
    private final Long writerId;
    private final String title;
    private final String content;
    private final LocalDateTime startTime;
    private final LocalDateTime endTime;
    private final Integer viewCount;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    // 생성자: 생성 시점에 모든 필드가 할당됨 (update 없음)
    public Board(Long id,
                 Long groupId,
                 Long writerId,
                 String title,
                 String content,
                 LocalDateTime startTime,
                 LocalDateTime endTime,
                 Integer viewCount,
                 LocalDateTime createdAt,
                 LocalDateTime updatedAt) {

        this.id = id;
        this.groupId = groupId;
        this.writerId = writerId;
        this.title = title;
        this.content = content;
        this.startTime = startTime;
        this.endTime = endTime;
        this.viewCount = viewCount;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // 최초 생성 시 사용할 static factory
    public static Board create(Long groupId,
                               Long writerId,
                               String title,
                               String content,
                               LocalDateTime endTime) {

        LocalDateTime now = LocalDateTime.now();

        return new Board(
                null,
                groupId,
                writerId,
                title,
                content,
                now,
                endTime,
                0,
                now,
                null
        );
    }

    // Repository에서 ID 할당할 때 새 객체를 만드는 방식
    public Board assignId(Long newId) {
        return new Board(
                newId,
                this.groupId,
                this.writerId,
                this.title,
                this.content,
                this.startTime,
                this.endTime,
                this.viewCount,
                this.createdAt,
                this.updatedAt
        );
    }

    // 수정: 불변 객체 → 수정 시 새 객체를 리턴하는 방식
    public Board update(String title, LocalDateTime endTime, String content) {
        return new Board(
                this.id,
                this.groupId,
                this.writerId,
                title,
                content,
                this.startTime,
                endTime,
                this.viewCount,
                this.createdAt,
                LocalDateTime.now()
        );
    }

    // 조회수 증가도 새 객체로
    public Board increaseViewCount() {
        return new Board(
                this.id,
                this.groupId,
                this.writerId,
                this.title,
                this.content,
                this.startTime,
                this.endTime,
                this.viewCount + 1,
                this.createdAt,
                this.updatedAt
        );
    }
}
