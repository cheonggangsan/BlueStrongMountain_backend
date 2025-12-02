package com.ssafy.BlueStrongMountain.exception;

public class UserNotInGroupException extends RuntimeException {
    public UserNotInGroupException(final Long userId, final Long groupId) {
        super("User is not in group. userId=" + userId + ", groupId=" + groupId);
    }
}
