package com.ssafy.BlueStrongMountain.service;

import com.ssafy.BlueStrongMountain.dto.*;

public interface AuthService {
    RegisterResponse register(RegisterRequest req);
    LoginResponse login(LoginRequest req);
    boolean confirmPassword(PasswordVerifyRequest req);
    UsernameDuplicateResponse checkUsername(String username);
    void logout(LogoutRequest req);
    void resetPasswordByEmail(String email);
}
