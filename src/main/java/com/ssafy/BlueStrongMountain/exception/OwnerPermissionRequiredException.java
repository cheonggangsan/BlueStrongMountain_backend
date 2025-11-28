package com.ssafy.BlueStrongMountain.exception;

public class OwnerPermissionRequiredException extends RuntimeException {
    public OwnerPermissionRequiredException() {
        super("Owner permission is required.");
    }
}
