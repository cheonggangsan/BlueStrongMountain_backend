package com.ssafy.BlueStrongMountain.service;

import com.ssafy.BlueStrongMountain.dto.*;

public interface AuthService {
    public RegisterResponse register(RegisterRequest req);
    public LoginResponse login(LoginRequest req);
    public UsernameDuplicateResponse checkUsername(String username);
    void logout(LogoutRequest req);
}
