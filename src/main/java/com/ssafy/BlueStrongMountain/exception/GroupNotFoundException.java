package com.ssafy.BlueStrongMountain.exception;

public class GroupNotFoundException extends RuntimeException {
    public GroupNotFoundException(final Long groupId) {
        super("Group not found. groupId=" + groupId);
    }
}
