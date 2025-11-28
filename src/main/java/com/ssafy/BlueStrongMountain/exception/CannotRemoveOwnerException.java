package com.ssafy.BlueStrongMountain.exception;

public class CannotRemoveOwnerException extends RuntimeException {
    public CannotRemoveOwnerException(final Long userId, final Long groupId) {
        super("Cannot remove owner from group. userId=" + userId + ", groupId=" + groupId);
    }
}
