package com.ssafy.BlueStrongMountain.controller;

import com.ssafy.BlueStrongMountain.dto.*;
import com.ssafy.BlueStrongMountain.service.AuthService;
import com.ssafy.BlueStrongMountain.service.SolvedAcSyncService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@AllArgsConstructor
public class AuthController {
    private final AuthService authService;
    private final SolvedAcSyncService solvedAcSyncService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest req) {
        return ResponseEntity.ok(authService.login(req));
    }

    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> register(@RequestBody RegisterRequest req){
        return ResponseEntity.ok(authService.register(req));
    }

    @GetMapping("/duplicate/username")
    public ResponseEntity<UsernameDuplicateResponse> dup(
            @RequestParam String username
    ) {
        return ResponseEntity.ok(authService.checkUsername(username));
    }
    /**
     * solved.ac baekjoon handle 확인 테스트
     *
     *
     */
    @GetMapping("/existHandle")
    public ResponseEntity<Boolean> checkBaekHandle(
            @RequestParam String handle
    ){
        return ResponseEntity.ok(solvedAcSyncService.existSolvedAcUser(handle));
    }

    @PostMapping("/logout")
    public ResponseEntity<BaseResponse> logout(
            @RequestBody @Valid LogoutRequest req
    ){
        authService.logout(req);
        return ResponseEntity.ok(BaseResponse.ok());
    }

    @PostMapping("/password/reset")
    public ResponseEntity<BaseResponse> resetPassword(
            @RequestBody @Valid PasswordResetRequest req
    ){
        authService.resetPasswordByEmail(req.getEmail());
        return ResponseEntity.ok(BaseResponse.ok());
    }
}
