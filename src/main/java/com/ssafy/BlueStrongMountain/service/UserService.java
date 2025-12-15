package com.ssafy.BlueStrongMountain.service;

import com.ssafy.BlueStrongMountain.dto.UserInfoResponse;
import com.ssafy.BlueStrongMountain.dto.UserSimpleDto;

import java.util.List;

public interface UserService {
    public UserInfoResponse getUser(Long userId);
    public List<UserSimpleDto> searchUsersByKeyword(String keyword);
    public void changeUsername(Long userId, String newName);
    public void changePassword(Long userId, String newPw);
    public void deleteUser(Long userId);
}
