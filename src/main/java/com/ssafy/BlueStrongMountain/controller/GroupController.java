package com.ssafy.BlueStrongMountain.controller;

import com.ssafy.BlueStrongMountain.dto.GroupCreateRequest;
import com.ssafy.BlueStrongMountain.dto.GroupSummaryDto;
import com.ssafy.BlueStrongMountain.dto.GroupUpdateRequest;
import com.ssafy.BlueStrongMountain.service.GroupMemberService;
import com.ssafy.BlueStrongMountain.service.GroupService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/groups")
public class GroupController {

    private final GroupService groupService;
    private final GroupMemberService groupMemberService;

    public GroupController(
            final GroupService groupService,
            final GroupMemberService groupMemberService
    ) {
        this.groupService = groupService;
        this.groupMemberService = groupMemberService;
    }

    /**
     * 그룹 생성
     */
    @PostMapping
    public ResponseEntity<?> createGroup(
            @RequestParam Long requesterId,
            @RequestBody GroupCreateRequest request
    ) {
        groupService.createGroup(requesterId, request);
        return ResponseEntity.ok().body(success());
    }

    /**
     * 내가 속한 그룹 전체 조회
     */
    @GetMapping
    public ResponseEntity<List<GroupSummaryDto>> findMyGroups(
            @RequestParam Long requesterId,
            @RequestParam(required = false) String name
    ) {
        //test
        List<GroupSummaryDto> tmpDtoList = groupService.searchMyGroups(requesterId, name);



        if (name != null && !name.isBlank()) {
            return ResponseEntity.ok(groupService.searchMyGroups(requesterId, name));
        }
        return ResponseEntity.ok(groupService.findMyGroups(requesterId));
    }

    /**
     * 그룹 수정 (OWNER)
     */
    @PatchMapping("/{groupId}")
    public ResponseEntity<?> updateGroup(
            @RequestParam Long requesterId,
            @PathVariable Long groupId,
            @RequestBody GroupUpdateRequest request
    ) {
        groupService.updateGroup(requesterId, groupId, request);
        return ResponseEntity.ok(success());
    }

    /**
     * 그룹 소유권 이전
     */
    @PatchMapping("/{groupId}/owner")
    public ResponseEntity<?> changeOwner(
            @RequestParam Long requesterId,
            @PathVariable Long groupId,
            @RequestBody(required = true) OwnerChangeRequest req
    ) {
        groupService.changeOwner(requesterId, groupId, req.getNewOwnerId());
        return ResponseEntity.ok(success());
    }

    /**
     * Manager 추가
     */
    @PostMapping("/{groupId}/managers")
    public ResponseEntity<?> addManager(
            @RequestParam Long requesterId,
            @PathVariable Long groupId,
            @RequestBody UserIdRequest request
    ) {
        groupMemberService.addManager(requesterId, groupId, request.getUserId());
        return ResponseEntity.ok(success());
    }

    /**
     * Manager 삭제 (bulk)
     */
    @DeleteMapping("/{groupId}/managers")
    public ResponseEntity<?> removeManagers(
            @RequestParam Long requesterId,
            @PathVariable Long groupId,
            @RequestBody ManagerIdsRequest request
    ) {
        groupMemberService.removeManagers(requesterId, groupId, request.getManagerIds());
        return ResponseEntity.ok(success());
    }

    /**
     * Member 추가
     */
    @PostMapping("/{groupId}/members")
    public ResponseEntity<?> addMember(
            @RequestParam Long requesterId,
            @PathVariable Long groupId,
            @RequestBody UserIdRequest request
    ) {
        groupMemberService.addMember(requesterId, groupId, request.getUserId());
        return ResponseEntity.ok(success());
    }

    /**
     * Member 강제 삭제
     */
    @DeleteMapping("/{groupId}/members/{memberId}")
    public ResponseEntity<?> removeMember(
            @RequestParam Long requesterId,
            @PathVariable Long groupId,
            @PathVariable Long memberId
    ) {
        groupMemberService.removeMember(requesterId, groupId, memberId);
        return ResponseEntity.ok(success());
    }

    /**
     * 본인 탈퇴
     */
    @DeleteMapping("/{groupId}/members/me")
    public ResponseEntity<?> leaveGroup(
            @RequestParam Long requesterId,
            @PathVariable Long groupId
    ) {
        groupMemberService.leaveGroup(requesterId, groupId);
        return ResponseEntity.ok(success());
    }

    /**
     * 공통 응답
     */
    private static Res success() {
        return new Res(true);
    }

    public static class Res {
        private final boolean success;

        public Res(final boolean success) {
            this.success = success;
        }

        public boolean isSuccess() {
            return success;
        }
    }

    public static class UserIdRequest {
        private Long userId;

        public Long getUserId() {
            return userId;
        }
    }

    public static class ManagerIdsRequest {
        private java.util.List<Long> managerIds;

        public java.util.List<Long> getManagerIds() {
            return managerIds;
        }
    }

    public static class OwnerChangeRequest {
        private Long newOwnerId;

        public Long getNewOwnerId() {
            return newOwnerId;
        }
    }
}

