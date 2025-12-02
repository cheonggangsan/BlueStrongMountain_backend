package com.ssafy.BlueStrongMountain.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssafy.BlueStrongMountain.dto.GroupCreateRequest;
import com.ssafy.BlueStrongMountain.dto.GroupSummaryDto;
import com.ssafy.BlueStrongMountain.dto.GroupUpdateRequest;
import com.ssafy.BlueStrongMountain.service.GroupMemberService;
import com.ssafy.BlueStrongMountain.service.GroupService;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(GroupController.class)
class GroupControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private GroupService groupService;

    @MockitoBean
    private GroupMemberService groupMemberService;

    @Autowired
    private ObjectMapper objectMapper;

    /**
     * [테스트 목적]
     * - POST /api/v1/groups
     * - requesterId를 query parameter로 넘기고, GroupCreateRequest를 body로 넘겼을 때
     * - GroupService.createGroup 이 올바르게 호출되는지
     * - 응답으로 {"success": true} JSON 이 내려오는지 검증
     */
    @Test
    @DisplayName("그룹 생성 API - 성공")
    void createGroup_success() throws Exception {
        // given
        Long requesterId = 1L;

        // GroupCreateRequest(title, managerIds, memberIds, visibility, description)
        GroupCreateRequest request = new GroupCreateRequest(
                "알고리즘 스터디",
                List.of(2L, 3L),              // managerIds
                List.of(4L, 5L),              // memberIds
                "PUBLIC",                     // visibility
                "매주 화요일 문제 풀이"        // description
        );

        given(groupService.createGroup(eq(requesterId), any(GroupCreateRequest.class)))
                .willReturn(10L);

        // when & then
        mockMvc.perform(post("/api/v1/groups")
                        .param("requesterId", String.valueOf(requesterId))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));

        then(groupService).should()
                .createGroup(eq(requesterId), any(GroupCreateRequest.class));
    }

    /**
     * [테스트 목적]
     * - GET /api/v1/groups?requesterId=1 (name 없음)
     * - name 파라미터가 없을 때 groupService.findMyGroups()가 호출되는지
     * - 응답이 List<GroupSummaryDto> JSON 배열로 내려오는지 검증
     */
    @Test
    @DisplayName("내가 속한 그룹 조회 - name 없이 전체 조회")
    void findMyGroups_withoutName() throws Exception {
        // given
        Long requesterId = 1L;

        // GroupSummaryDto는 private 생성자라 new 불가 → mock으로 대체
        GroupSummaryDto dto1 = Mockito.mock(GroupSummaryDto.class);
        GroupSummaryDto dto2 = Mockito.mock(GroupSummaryDto.class);

        given(groupService.findMyGroups(requesterId))
                .willReturn(List.of(dto1, dto2));

        // when & then
        mockMvc.perform(get("/api/v1/groups")
                        .param("requesterId", String.valueOf(requesterId)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2)); // 배열 길이만 검증

        then(groupService).should()
                .findMyGroups(requesterId);
        then(groupService).shouldHaveNoMoreInteractions();
    }

    /**
     * [테스트 목적]
     * - GET /api/v1/groups?requesterId=1&name=algo
     * - name 파라미터가 존재할 때 groupService.searchMyGroups()가 호출되는지
     * - 응답이 검색 결과 List<GroupSummaryDto> JSON 배열인지 검증
     */
    @Test
    @DisplayName("내가 속한 그룹 조회 - name으로 검색")
    void findMyGroups_withName() throws Exception {
        // given
        Long requesterId = 1L;
        String name = "알고";

        GroupSummaryDto dto = Mockito.mock(GroupSummaryDto.class);

        given(groupService.searchMyGroups(requesterId, name))
                .willReturn(List.of(dto));

        // when & then
        mockMvc.perform(get("/api/v1/groups")
                        .param("requesterId", String.valueOf(requesterId))
                        .param("name", name))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));

        then(groupService).should()
                .searchMyGroups(requesterId, name);
        then(groupService).shouldHaveNoMoreInteractions();
    }

    /**
     * [테스트 목적]
     * - PATCH /api/v1/groups/{groupId}
     * - 그룹 수정 요청이 들어왔을 때 groupService.updateGroup이 올바르게 호출되는지
     * - 응답으로 {"success": true}가 내려오는지 검증
     *
     * GroupUpdateRequest(title, visibility, description) 순서 주의
     */
    @Test
    @DisplayName("그룹 수정 API - 성공")
    void updateGroup_success() throws Exception {
        // given
        Long requesterId = 1L;
        Long groupId = 100L;

        GroupUpdateRequest request = new GroupUpdateRequest(
                "수정된 스터디 이름",
                "PRIVATE",
                "수정된 설명"
        );

        // when & then
        mockMvc.perform(patch("/api/v1/groups/{groupId}", groupId)
                        .param("requesterId", String.valueOf(requesterId))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));

        then(groupService).should()
                .updateGroup(eq(requesterId), eq(groupId), any(GroupUpdateRequest.class));
    }

    /**
     * [테스트 목적]
     * - PATCH /api/v1/groups/{groupId}/owner
     * - 소유권 변경 요청이 들어왔을 때 groupService.changeOwner가 올바르게 호출되는지
     * - newOwnerId가 JSON body에서 제대로 바인딩되는지
     * - 응답으로 {"success": true}가 내려오는지 검증
     */
    @Test
    @DisplayName("그룹 소유권 변경 API - 성공")
    void changeOwner_success() throws Exception {
        // given
        Long requesterId = 1L;
        Long groupId = 100L;
        Long newOwnerId = 2L;

        String body = """
                {"newOwnerId": %d}
                """.formatted(newOwnerId);

        // when & then
        mockMvc.perform(patch("/api/v1/groups/{groupId}/owner", groupId)
                        .param("requesterId", String.valueOf(requesterId))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));

        then(groupService).should()
                .changeOwner(requesterId, groupId, newOwnerId);
    }

    /**
     * [테스트 목적]
     * - POST /api/v1/groups/{groupId}/managers
     * - Manager 추가 요청이 들어왔을 때 groupMemberService.addManagers가
     *   requesterId, groupId, userIds로 올바르게 호출되는지 검증
     */
    @Test
    @DisplayName("Manager 추가 API - 성공")
    void addManagers_success() throws Exception {
        // given
        Long requesterId = 1L;
        Long groupId = 100L;
        List<Long> userIds = List.of(2L, 3L);

        String body = objectMapper.writeValueAsString(
                new UserIdsRequestStub(userIds)
        );

        // when & then
        mockMvc.perform(post("/api/v1/groups/{groupId}/managers", groupId)
                        .param("requesterId", String.valueOf(requesterId))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));

        then(groupMemberService).should()
                .addManagers(eq(requesterId), eq(groupId), eq(userIds));
    }

    /**
     * [테스트 목적]
     * - DELETE /api/v1/groups/{groupId}/managers
     * - Manager 삭제(bulk) 요청이 들어왔을 때 groupMemberService.removeManagers가
     *   requesterId, groupId, managerIds로 올바르게 호출되는지 검증
     */
    @Test
    @DisplayName("Manager 삭제 API - 성공")
    void removeManagers_success() throws Exception {
        // given
        Long requesterId = 1L;
        Long groupId = 100L;
        List<Long> managerIds = List.of(2L, 3L);

        String body = """
                {"managerIds": [2, 3]}
                """;

        // when & then
        mockMvc.perform(delete("/api/v1/groups/{groupId}/managers", groupId)
                        .param("requesterId", String.valueOf(requesterId))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));

        then(groupMemberService).should()
                .removeManagers(eq(requesterId), eq(groupId), eq(managerIds));
    }

    /**
     * [테스트 목적]
     * - POST /api/v1/groups/{groupId}/members
     * - Member 추가 요청이 들어왔을 때 groupMemberService.addMembers가
     *   requesterId, groupId, userIds로 올바르게 호출되는지 검증
     */
    @Test
    @DisplayName("Member 추가 API - 성공")
    void addMembers_success() throws Exception {
        // given
        Long requesterId = 1L;
        Long groupId = 100L;
        List<Long> userIds = List.of(4L, 5L);

        String body = objectMapper.writeValueAsString(
                new UserIdsRequestStub(userIds)
        );

        // when & then
        mockMvc.perform(post("/api/v1/groups/{groupId}/members", groupId)
                        .param("requesterId", String.valueOf(requesterId))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));

        then(groupMemberService).should()
                .addMembers(eq(requesterId), eq(groupId), eq(userIds));
    }

    /**
     * [테스트 목적]
     * - DELETE /api/v1/groups/{groupId}/members
     * - Member 강제 삭제 요청이 들어왔을 때 groupMemberService.removeMembers가
     *   requesterId, groupId, userIds로 올바르게 호출되는지 검증
     */
    @Test
    @DisplayName("Member 강제 삭제 API - 성공")
    void removeMembers_success() throws Exception {
        // given
        Long requesterId = 1L;
        Long groupId = 100L;
        List<Long> userIds = List.of(4L, 5L);

        String body = objectMapper.writeValueAsString(
                new UserIdsRequestStub(userIds)
        );

        // when & then
        mockMvc.perform(delete("/api/v1/groups/{groupId}/members", groupId)
                        .param("requesterId", String.valueOf(requesterId))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));

        then(groupMemberService).should()
                .removeMembers(eq(requesterId), eq(groupId), eq(userIds));
    }

    /**
     * [테스트 목적]
     * - DELETE /api/v1/groups/{groupId}/members/me
     * - 본인 탈퇴 요청이 들어왔을 때 groupMemberService.leaveGroup이
     *   requesterId, groupId로 올바르게 호출되는지 검증
     */
    @Test
    @DisplayName("그룹 탈퇴 API - 성공")
    void leaveGroup_success() throws Exception {
        // given
        Long requesterId = 1L;
        Long groupId = 100L;

        // when & then
        mockMvc.perform(delete("/api/v1/groups/{groupId}/members/me", groupId)
                        .param("requesterId", String.valueOf(requesterId)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));

        then(groupMemberService).should()
                .leaveGroup(requesterId, groupId);
    }

    /**
     * 컨트롤러 내부 static class(UserIdsRequest)가 setter가 없어
     * 테스트 코드에서 JSON 직렬화 용으로 사용하는 간단한 Stub 클래스
     */
    private static class UserIdsRequestStub {
        private final List<Long> userIds;

        public UserIdsRequestStub(List<Long> userIds) {
            this.userIds = userIds;
        }

        public List<Long> getUserIds() {
            return userIds;
        }
    }
}
