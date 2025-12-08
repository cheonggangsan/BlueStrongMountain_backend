package com.ssafy.BlueStrongMountain.service;

import com.ssafy.BlueStrongMountain.dto.UserInfoResponse;

public interface UserService {
    public UserInfoResponse getUser(Long userId);
    public void changeUsername(Long userId, String newName);
    public void changePassword(Long userId, String newPw);
    public void deleteUser(Long userId);
}
