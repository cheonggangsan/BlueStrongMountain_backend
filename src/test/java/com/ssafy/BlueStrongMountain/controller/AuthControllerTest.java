package com.ssafy.BlueStrongMountain.controller;

import static org.junit.jupiter.api.Assertions.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssafy.BlueStrongMountain.domain.UserStatus;
import com.ssafy.BlueStrongMountain.dto.LoginRequest;
import com.ssafy.BlueStrongMountain.dto.LoginResponse;
import com.ssafy.BlueStrongMountain.dto.RegisterRequest;
import com.ssafy.BlueStrongMountain.dto.RegisterResponse;
import com.ssafy.BlueStrongMountain.dto.UsernameDuplicateResponse;
import com.ssafy.BlueStrongMountain.service.AuthService;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import org.springframework.http.MediaType;

import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

@WebMvcTest(AuthController.class)
// ★ Security 비활성화
@AutoConfigureMockMvc(addFilters = false)
class AuthControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockitoBean
    AuthService authService;


    // -------------------------------------------------------------------------
    // LOGIN
    // -------------------------------------------------------------------------
    @Test
    @DisplayName("POST /api/v1/auth/login - 로그인 성공")
    void login_success() throws Exception {

        LoginRequest req = new LoginRequest(
                "test@gmail.com",
                "test1234"
        );

        LoginResponse res = new LoginResponse(
                1L,
                "test@gmail.com",
                "tester",
                "ACCESS_TOKEN",
                "REFRESH_TOKEN"
        );

        //given(authService.login(req)).willReturn(res);
        given(authService.login(Mockito.any(LoginRequest.class)))
                .willReturn(res);

        mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value(1L))
                .andExpect(jsonPath("$.email").value("test@gmail.com"))
                .andExpect(jsonPath("$.username").value("tester"));
//                .andExpect(jsonPath("$.token").value("ACCESS_TOKEN"))
//                .andExpect(jsonPath("$.refreshToken").value("REFRESH_TOKEN"));
    }



    // -------------------------------------------------------------------------
    // REGISTER
    // -------------------------------------------------------------------------
    @Test
    @DisplayName("POST /api/v1/auth/register - 회원가입 성공")
    void register_success() throws Exception {

        RegisterRequest req = new RegisterRequest(
                "test@gmail.com",
                "test1234",
                "tester",
                "baekjoonID"
        );

        RegisterResponse res = new RegisterResponse(
                1L,
                "test@gmail.com",
                "tester",
                UserStatus.ACTIVE.name()
        );

        //given(authService.register(req)).willReturn(res);
        given(authService.register(Mockito.any(RegisterRequest.class)))
                .willReturn(res);

        mockMvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value(1L))
                .andExpect(jsonPath("$.email").value("test@gmail.com"))
                .andExpect(jsonPath("$.username").value("tester"));
    }



    // -------------------------------------------------------------------------
    // USERNAME DUPLICATE CHECK
    // -------------------------------------------------------------------------
    @Test
    @DisplayName("GET /api/v1/auth/duplicate/username - username 중복 체크 성공")
    void duplicate_username_success() throws Exception {

        String username = "tester";

        UsernameDuplicateResponse res = new UsernameDuplicateResponse(username, false);

        given(authService.checkUsername(username)).willReturn(res);

        mockMvc.perform(get("/api/v1/auth/duplicate/username")
                        .param("username", username))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.duplicated").value(false));
    }
}
