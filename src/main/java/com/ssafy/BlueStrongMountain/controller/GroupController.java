package com.ssafy.BlueStrongMountain.controller;

import com.ssafy.BlueStrongMountain.dto.*;
import com.ssafy.BlueStrongMountain.service.GroupMemberService;
import com.ssafy.BlueStrongMountain.service.GroupService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/groups")
@RequiredArgsConstructor
public class GroupController {

    private final GroupService groupService;
    private final GroupMemberService groupMemberService;


    /**
     * 그룹 생성
     */
    @PostMapping
    public ResponseEntity<BaseResponse> createGroup(
            @RequestParam Long requesterId,
            @RequestBody GroupCreateRequest request
    ) {
        Long createdGroupId = groupService.createGroup(requesterId, request);
        System.out.println("!!!!created group id!!!!");
        System.out.println(createdGroupId);

        return ResponseEntity.ok(BaseResponse.ok());
    }

    /**
     * 그룹 상세조회
     */
    @GetMapping("/detail/{groupId}")
    public ResponseEntity<GroupDetailDto> getGroupDetail(
            @RequestParam Long requesterId,
            @PathVariable Long groupId
    ){
        return ResponseEntity.ok(groupService.getGroupDetail(requesterId, groupId));
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
        //List<GroupSummaryDto> tmpDtoList;



        if (name != null && !name.isBlank()) {

            //tmpDtoList = groupService.searchMyGroups(requesterId, name);

            return ResponseEntity.ok(groupService.searchMyGroups(requesterId, name));
        }

        //tmpDtoList = groupService.findMyGroups(requesterId);
//        for(GroupSummaryDto gsd : tmpDtoList){
//            //System.out.println(gsd.toString());//test
//        }

        return ResponseEntity.ok(groupService.findMyGroups(requesterId));
    }

    /**
     * 그룹 수정 (OWNER)
     */
    @PatchMapping("/{groupId}")
    public ResponseEntity<BaseResponse> updateGroup(
            @RequestParam Long requesterId,
            @PathVariable Long groupId,
            @RequestBody GroupUpdateRequest request
    ) {
        groupService.updateGroup(requesterId, groupId, request);
        return ResponseEntity.ok(BaseResponse.ok());
    }

    /**
     * 그룹 소유권 이전
     */
    @PatchMapping("/{groupId}/owner")
    public ResponseEntity<BaseResponse> changeOwner(
            @RequestParam Long requesterId,
            @PathVariable Long groupId,
            @RequestBody(required = true) Long newOwnerId
    ) {
        groupService.changeOwner(requesterId, groupId, newOwnerId);
        return ResponseEntity.ok(BaseResponse.ok());
    }

    /**
     * Manager 추가
     */
    @PostMapping("/{groupId}/managers")
    public ResponseEntity<BaseResponse> addManager(
            @RequestParam Long requesterId,
            @PathVariable Long groupId,
            @RequestBody List<Long> newManagerIds
    ) {
        groupMemberService.addManagers(requesterId, groupId, newManagerIds);
        return ResponseEntity.ok(BaseResponse.ok());
    }

    /**
     * Manager 삭제 (bulk)
     */
    @DeleteMapping("/{groupId}/managers")
    public ResponseEntity<BaseResponse> removeManagers(
            @RequestParam Long requesterId,
            @PathVariable Long groupId,
            @RequestBody List<Long> delManagerIds
    ) {
        groupMemberService.removeManagers(requesterId, groupId, delManagerIds);
        return ResponseEntity.ok(BaseResponse.ok());
    }

    /**
     * Member 추가
     */
    @PostMapping("/{groupId}/members")
    public ResponseEntity<BaseResponse> addMember(
            @RequestParam Long requesterId,
            @PathVariable Long groupId,
            @RequestBody List<Long> newMemberIds
    ) {
        groupMemberService.addMembers(requesterId, groupId, newMemberIds);
        return ResponseEntity.ok(BaseResponse.ok());
    }

    /**
     * Member 강제 삭제
     */
    @DeleteMapping("/{groupId}/members")
    public ResponseEntity<BaseResponse> removeMember(
            @RequestParam Long requesterId,
            @PathVariable Long groupId,
            @RequestBody List<Long> delMemberIds
    ) {
        groupMemberService.removeMembers(requesterId, groupId, delMemberIds);
        return ResponseEntity.ok(BaseResponse.ok());
    }

    /**
     * 본인 탈퇴
     */
    @DeleteMapping("/{groupId}/members/me")
    public ResponseEntity<BaseResponse> leaveGroup(
            @RequestParam Long requesterId,
            @PathVariable Long groupId
    ) {
        groupMemberService.leaveGroup(requesterId, groupId);
        return ResponseEntity.ok(BaseResponse.ok());
    }
}

