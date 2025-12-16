package com.ssafy.BlueStrongMountain.service;

import com.ssafy.BlueStrongMountain.domain.*;
import com.ssafy.BlueStrongMountain.dto.GroupCreateRequest;
import com.ssafy.BlueStrongMountain.dto.GroupDetailDto;
import com.ssafy.BlueStrongMountain.dto.GroupSummaryDto;
import com.ssafy.BlueStrongMountain.dto.GroupUpdateRequest;
import com.ssafy.BlueStrongMountain.exception.GroupNotFoundException;
import com.ssafy.BlueStrongMountain.repository.BoardRepository;
import com.ssafy.BlueStrongMountain.repository.BoardUserProgressRepository;
import com.ssafy.BlueStrongMountain.repository.GroupRepository;
import com.ssafy.BlueStrongMountain.repository.UserGroupRepository;

import com.ssafy.BlueStrongMountain.service.validator.GroupAuthorityService;
import com.ssafy.BlueStrongMountain.service.validator.GroupValidator;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class GroupServiceImpl implements GroupService {

    private final GroupRepository groupRepository;
    private final UserGroupRepository userGroupRepository;
    private final GroupValidator groupValidator;
    private final GroupAuthorityService groupAuthorityService;

    private final BoardUserProgressRepository boardUserProgressRepository;
    private final BoardRepository boardRepository;


    @Override
    public Long createGroup(
            final Long ownerId,
            final GroupCreateRequest request
    ) {
        groupValidator.validateCreateRequest(ownerId, request);

        final LocalDateTime now = LocalDateTime.now();
        final GroupVisibility visibility = GroupVisibility.PRIVATE;

        final Group group = Group.create(
                request.getTitle(),
                request.getDescription(),
                visibility,
                ownerId,
                now
        );

        final Group saved = groupRepository.save(group);

        final UserGroup ownerUserGroup = UserGroup.create(ownerId, saved.getId(), GroupRole.OWNER, now);
        //userGroupRepository.save(ownerUserGroup);
        userGroupRepository.insert(ownerUserGroup);

        if (request.getManagerIds() != null) {
            for (Long managerId : request.getManagerIds()) {
                final UserGroup userGroup = UserGroup.create(managerId, saved.getId(), GroupRole.MANAGER, now);
                //userGroupRepository.save(userGroup);
                userGroupRepository.insert(userGroup);
            }
        }

        if (request.getMemberIds() != null) {
            for (Long memberId : request.getMemberIds()) {
                final UserGroup userGroup = UserGroup.create(memberId, saved.getId(), GroupRole.MEMBER, now);
                //userGroupRepository.save(userGroup);
                userGroupRepository.insert(userGroup);
            }
        }

        return saved.getId();
    }

    @Override
    public GroupDetailDto getGroupDetail(final Long requesterId, final Long groupId){
        groupAuthorityService.validateUserInGroup(requesterId, groupId);

        Group findGroup = groupRepository.findById(groupId)
                .orElseThrow(() -> new GroupNotFoundException(groupId));

        List<Long> memberIds = userGroupRepository.findByGroupIdAndRole(groupId, GroupRole.MEMBER)
                .stream()
                .map(UserGroup::getUserId)
                .toList();

        List<Long> managerIds = userGroupRepository.findByGroupIdAndRole(groupId, GroupRole.MANAGER)
                .stream()
                .map(UserGroup::getUserId)
                .toList();

        return new GroupDetailDto(
                findGroup.getId(),
                findGroup.getTitle(),
                findGroup.getDescription(),
                findGroup.getOwnerId(),
                managerIds,
                memberIds,
                findGroup.getVisibility().name(),
                findGroup.getCreatedAt().toString(),
                findGroup.getUpdatedAt().toString()
        );
    }

    @Override
    public List<GroupSummaryDto> findMyGroups(final Long requesterId) {
        final List<Long> myGroupIds = userGroupRepository.findByUserId(requesterId).stream()
                .map(UserGroup::getGroupId)
                .toList();

        if (myGroupIds.isEmpty()) {
            return List.of();
        }

        final List<Group> groups = groupRepository.findByIds(myGroupIds);

        final List<GroupSummaryDto> result = new ArrayList<>();
        for (Group group : groups) {
            final int memberCount = userGroupRepository.countByGroupId(group.getId());
            result.add(GroupSummaryDto.from(group, memberCount));
        }

//        //test
//        System.out.println("group size in findMyGroups = " + groups.size());
//        for(Group group : groups){
//            final int memberCount = userGroupRepository.countByGroupId(group.getId());
//            System.out.println("group id : " +group.getId() + " memberCount = " + memberCount);
//        }
//        //test

        result.sort(Comparator.comparing(GroupSummaryDto::getUpdatedAt).reversed());

        return result;
    }

    @Override
    public List<GroupSummaryDto> searchMyGroups(
            final Long requesterId,
            final String name
    ) {
        final List<GroupSummaryDto> myGroups = findMyGroups(requesterId);

        if (name == null || name.isBlank()) {
            return myGroups;
        }

        final String keyword = name.toLowerCase();
        final List<GroupSummaryDto> filtered = new ArrayList<>();

        for (GroupSummaryDto dto : myGroups) {
            if (dto.getTitle().toLowerCase().contains(keyword)) {
                filtered.add(dto);
            }
        }

        return filtered;
    }

    @Override
    public void updateGroup(
            final Long requesterId,
            final Long groupId,
            final GroupUpdateRequest request
    ) {
        groupValidator.validateUpdateRequest(requesterId, request);
        groupAuthorityService.validateOwner(requesterId, groupId);


        final Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new GroupNotFoundException(groupId));

        final LocalDateTime now = LocalDateTime.now();

        
        //기존 유저 조회
        List<UserGroup> oldGroupUsers =
                userGroupRepository.findByGroupId(groupId);

        Map<Long, UserGroup> oldUserMap = oldGroupUsers.stream()
                .collect(Collectors.toMap(UserGroup::getUserId, ug -> ug));

        Set<Long> oldManagerIds = oldGroupUsers.stream()
                .filter(ug -> ug.getRole() == GroupRole.MANAGER)
                .map(UserGroup::getUserId)
                .collect(Collectors.toSet());

        Set<Long> oldMemberIds = oldGroupUsers.stream()
                .filter(ug -> ug.getRole() == GroupRole.MEMBER)
                .map(UserGroup::getUserId)
                .collect(Collectors.toSet());


        //요청 사용자
        Set<Long> newManagerIds = new HashSet<>(request.getManagerIds());
        Set<Long> newMemberIds = new HashSet<>(request.getMemberIds());

        //역할 변경
        for(Long userId : newManagerIds){
            if(oldMemberIds.contains(userId)){
                UserGroup ug = oldUserMap.get(userId);
                ug.changeRole(GroupRole.MANAGER);
                userGroupRepository.updateRole(ug);
            }
        }

        for(Long userId : newMemberIds){
            if(oldManagerIds.contains(userId)){
                UserGroup ug = oldUserMap.get(userId);
                ug.changeRole(GroupRole.MEMBER);
                userGroupRepository.updateRole(ug);
            }
        }


        //신규 추가
        for(Long userId : newManagerIds){
            if(!oldUserMap.containsKey(userId)){
                userGroupRepository.insert(
                        UserGroup.create(
                                userId,
                                groupId,
                                GroupRole.MANAGER,
                                now
                        )
                );
            }
        }

        for(Long userId : newMemberIds){
            if(!oldUserMap.containsKey(userId)){
                userGroupRepository.insert(
                        UserGroup.create(
                                userId,
                                groupId,
                                GroupRole.MEMBER,
                                now
                        )
                );
            }
        }

        //기존 삭제
        Set<Long> newAll = new HashSet<>();
        newAll.addAll(newManagerIds);
        newAll.addAll(newMemberIds);

        List<Long> toDeleteIds = oldUserMap.keySet().stream()
                .filter(userId -> (!newAll.contains(userId)))
                .filter(userId -> !userId.equals(requesterId))
                .toList();
        if(!toDeleteIds.isEmpty()){
            userGroupRepository.deleteByGroupIdAndUserIdIn(groupId, toDeleteIds);
        }

        //BoardUserProgress 삭제
        cleanupBoardUserProgress(groupId, toDeleteIds);

        //그룹 자체 업데이트
        group.update(
                request.getTitle(),
                request.getDescription(),
                GroupVisibility.PRIVATE,
                now
        );

//        //test
//        System.out.println("update user!!!!! test");
//        System.out.println(group.toString());

        groupRepository.save(group);
    }

    private void cleanupBoardUserProgress(
            Long groupId,
            List<Long> removedUserIds
    ){
        List<Long> boardIds = boardRepository.findByGroupId(groupId)
                .stream()
                .map(Board::getId)
                .toList();

        for(Long boardId : boardIds){
            for(Long userId : removedUserIds){
                cleanupUserProgressInBoard(boardId, userId);
            }
        }

    }
    private void cleanupUserProgressInBoard(
            Long boardId,
            Long userId
    ){
        List<Long> problemIds =
                boardUserProgressRepository.findProblemIdsByBoardAndUser(
                        boardId,
                        userId
                );
        if(problemIds.isEmpty()){
            return;
        }
        boardUserProgressRepository.deleteByBoardAndUser(
                boardId,
                userId
        );
    }


    @Override
    public void changeOwner(
            final Long requesterId,
            final Long groupId,
            final Long newOwnerId
    ) {
        groupAuthorityService.validateOwnerTransferable(requesterId, groupId, newOwnerId);

        final Group findGroup = groupRepository.findById(groupId)
                .orElseThrow(() -> new GroupNotFoundException(groupId));

        final LocalDateTime now = LocalDateTime.now();
        findGroup.changeOwner(newOwnerId, now);
        groupRepository.save(findGroup);

//        //test
//        System.out.println("group update test!!!!!!!!!!!!!!!!");
//        System.out.println(findGroup.toString());
//        System.out.println("group update test_end!!!!!!!!!!!!!!!!");
//        //test

//        //test
//        System.out.println("before group size" + userGroupRepository.findByUserId(requesterId).size());
//        groupRepository.save(group);
//        System.out.println("after group size" + userGroupRepository.findByUserId(requesterId).size());
//        //test

        final UserGroup oldOwner = userGroupRepository.findByUserIdAndGroupId(requesterId, groupId)
                .orElseThrow(() -> new GroupNotFoundException(groupId));
        oldOwner.changeRole(GroupRole.MEMBER);

        userGroupRepository.updateRole(oldOwner);

        final UserGroup newOwner = userGroupRepository.findByUserIdAndGroupId(newOwnerId, groupId)
                .orElseThrow(() -> new GroupNotFoundException(groupId));
        newOwner.changeRole(GroupRole.OWNER);

        userGroupRepository.updateRole(newOwner);
//
//        userGroupRepository.save(oldOwner);
//        userGroupRepository.save(newOwner);
    }
}
