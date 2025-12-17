package com.ssafy.BlueStrongMountain.exception;

public class ManagerPermissionRequiredException extends RuntimeException{
    public ManagerPermissionRequiredException(){
        super("Manager or higher permission is required.");
    }
}
