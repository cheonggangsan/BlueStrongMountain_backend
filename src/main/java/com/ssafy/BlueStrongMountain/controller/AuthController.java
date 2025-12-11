package com.ssafy.BlueStrongMountain.controller;

import com.ssafy.BlueStrongMountain.dto.*;
import com.ssafy.BlueStrongMountain.service.AuthService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@AllArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest req) {
        return ResponseEntity.ok(authService.login(req));
    }

    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> register(@RequestBody RegisterRequest req){

        //test
        System.out.println("!!!!!!!!!register user");
        System.out.println(req.toString());

        return ResponseEntity.ok(authService.register(req));
    }

    @GetMapping("/duplicate/username")
    public ResponseEntity<UsernameDuplicateResponse> dup(
            @RequestParam String username
    ) {
        return ResponseEntity.ok(authService.checkUsername(username));
    }

    @PostMapping("/logout")
    public ResponseEntity<BaseResponse> logout(
            @RequestBody LogoutRequest req
    ){
        authService.logout(req);
        return ResponseEntity.ok(BaseResponse.ok());
    }
}
