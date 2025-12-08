package com.ssafy.BlueStrongMountain.controller;

import static org.junit.jupiter.api.Assertions.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssafy.BlueStrongMountain.domain.UserStatus;
import com.ssafy.BlueStrongMountain.dto.ChangePasswordRequest;
import com.ssafy.BlueStrongMountain.dto.ChangeUsernameRequest;
import com.ssafy.BlueStrongMountain.dto.UserInfoResponse;
import com.ssafy.BlueStrongMountain.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import org.springframework.http.MediaType;

import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
//spring security 비활성화
@AutoConfigureMockMvc(addFilters = false)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private UserService userService;


    @Test
    @DisplayName("GET /api/v1/users/{id} - 유저 조회 성공")
    void getUser_success() throws Exception {
        // given
        Long userId = 1L;
        UserInfoResponse mockResponse = new UserInfoResponse(
                userId,
                "test@gmail.com",
                "tester",
                "baekjoonID",
                UserStatus.ACTIVE.name(),
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        given(userService.getUser(userId)).willReturn(mockResponse);

        // when & then
        mockMvc.perform(get("/api/v1/users/{id}", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value(userId))
                .andExpect(jsonPath("$.email").value("test@gmail.com"))
                .andExpect(jsonPath("$.username").value("tester"))
                .andExpect(jsonPath("$.baekjoonHandle").value("baekjoonID"));
    }



    @Test
    @DisplayName("PATCH /api/v1/users/{id}/username - 이름 변경 성공")
    void changeUsername_success() throws Exception {
        // given
        Long userId = 1L;
        ChangeUsernameRequest req = new ChangeUsernameRequest("newName");

        doNothing().when(userService).changeUsername(userId, "newName");

        // when & then
        mockMvc.perform(patch("/api/v1/users/{id}/username", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));
    }



    @Test
    @DisplayName("PATCH /api/v1/users/{id}/password - 비밀번호 변경 성공")
    void changePassword_success() throws Exception {
        // given
        Long userId = 1L;
        ChangePasswordRequest req = new ChangePasswordRequest("newPassword123");

        doNothing().when(userService).changePassword(userId, "newPassword123");

        // when & then
        mockMvc.perform(patch("/api/v1/users/{id}/password", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));
    }



    @Test
    @DisplayName("DELETE /api/v1/users/{id} - 유저 삭제 성공")
    void deleteUser_success() throws Exception {
        // given
        Long userId = 1L;
        doNothing().when(userService).deleteUser(userId);

        // when & then
        mockMvc.perform(delete("/api/v1/users/{id}", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));
    }
}
