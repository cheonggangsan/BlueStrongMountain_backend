package com.ssafy.BlueStrongMountain.domain;

import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class UserGroup {

    private Long userId;
    private Long groupId;
    private GroupRole role;
    private LocalDateTime joinedAt;

    protected UserGroup() {
    }

    private UserGroup(
            final Long userId,
            final Long groupId,
            final GroupRole role,
            final LocalDateTime joinedAt
    ) {
        this.userId = userId;
        this.groupId = groupId;
        this.role = role;
        this.joinedAt = joinedAt;
    }

    public static UserGroup create(
            final Long userId,
            final Long groupId,
            final GroupRole role,
            final LocalDateTime joinedAt
    ) {
        return new UserGroup(userId, groupId, role, joinedAt);
    }

    public void changeRole(final GroupRole role) {
        this.role = role;
    }
}
