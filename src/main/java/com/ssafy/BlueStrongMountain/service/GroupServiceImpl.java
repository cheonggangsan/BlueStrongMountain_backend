package com.ssafy.BlueStrongMountain.service;

import com.ssafy.BlueStrongMountain.domain.*;
import com.ssafy.BlueStrongMountain.dto.GroupCreateRequest;
import com.ssafy.BlueStrongMountain.dto.GroupDetailDto;
import com.ssafy.BlueStrongMountain.dto.GroupSummaryDto;
import com.ssafy.BlueStrongMountain.dto.GroupUpdateRequest;
import com.ssafy.BlueStrongMountain.exception.GroupNotFoundException;
import com.ssafy.BlueStrongMountain.repository.*;

import com.ssafy.BlueStrongMountain.service.validator.GroupAuthorityService;
import com.ssafy.BlueStrongMountain.service.validator.GroupValidator;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class GroupServiceImpl implements GroupService {

    private final GroupRepository groupRepository;
    private final UserGroupRepository userGroupRepository;
    private final GroupValidator groupValidator;
    private final GroupAuthorityService groupAuthorityService;

    private final BoardUserProgressRepository boardUserProgressRepository;
    private final BoardRepository boardRepository;
    private final BoardProblemRepository boardProblemRepository;


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
        //FIXME db 접근 비효율성
        List<UserGroup> myGroups = userGroupRepository.findByUserId(requesterId);
        final List<Long> myGroupIds = myGroups.stream()
                .map(UserGroup::getGroupId)
                .toList();

        if (myGroupIds.isEmpty()) {
            return List.of();
        }

        final List<Group> groups = groupRepository.findByIds(myGroupIds);

        Map<Long, Group> groupMap = groups.stream()
                .collect(Collectors.toMap(Group::getId, Function.identity()));

        Map<Long, GroupRole> myGroupRoleMap = myGroups.stream()
                .collect(Collectors.toMap(UserGroup::getGroupId, UserGroup::getRole));

        final List<GroupSummaryDto> result = new ArrayList<>();

        for(Long groupId : myGroupIds){
            final int memberCount = userGroupRepository.countByGroupId(groupId);
            GroupRole myGroupRole = myGroupRoleMap.get(groupId);

            result.add(GroupSummaryDto.from(
                    groupMap.get(groupId),
                    myGroupRole,
                    memberCount)
            );
        }


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
        Set<Long> newManagerIds =
                request.getManagerIds() == null
                        ? Set.of()
                        : new HashSet<>(request.getManagerIds());

        Set<Long> newMemberIds =
                request.getMemberIds() == null
                        ? Set.of()
                        : new HashSet<>(request.getMemberIds());

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

        List<Long> newUserIds = newAll.stream()
                .filter(userId -> (!oldUserMap.containsKey(userId)))
                .toList();


        addBoardUserProgress(groupId, newUserIds);


        //기존 삭제
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

        groupRepository.save(group);
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


        final UserGroup oldOwner = userGroupRepository.findByUserIdAndGroupId(requesterId, groupId)
                .orElseThrow(() -> new GroupNotFoundException(groupId));
        oldOwner.changeRole(GroupRole.MEMBER);

        userGroupRepository.updateRole(oldOwner);

        final UserGroup newOwner = userGroupRepository.findByUserIdAndGroupId(newOwnerId, groupId)
                .orElseThrow(() -> new GroupNotFoundException(groupId));
        newOwner.changeRole(GroupRole.OWNER);

        userGroupRepository.updateRole(newOwner);
    }

    @Override
    @Transactional
    public void deleteGroup(Long requesterId, Long groupId) {
        //owner 검증
        groupAuthorityService.validateOwner(requesterId, groupId);

        List<Long> deleteUserIds =
                userGroupRepository.findByGroupId(groupId).stream()
                        .map(UserGroup::getUserId)
                        .toList();

        cleanupBoardUserProgress(groupId, deleteUserIds);

        //FIXME n+1 문제 있음
        for(Long userId : deleteUserIds){
            userGroupRepository.deleteByUserIdAndGroupId(userId, groupId);
        }
        groupRepository.deleteById(groupId);
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


    private void addBoardUserProgress(
            Long groupId,
            List<Long> newUserIds
    ){
        List<Board> boards = boardRepository.findByGroupId(groupId);
        LocalDateTime curTime = LocalDateTime.now();

        for(Board board : boards){
            if(curTime.isAfter(board.getEndTime()))
                continue;
            for(Long userId : newUserIds){
                addUserProgressInBoard(board.getId(), userId);
            }
        }
    }

    private void addUserProgressInBoard(
            Long boardId,
            Long userId
    ){
        List<Long> problemIds =
                boardProblemRepository.findByBoardId(boardId)
                        .stream()
                        .map(BoardProblem::getProblemId)
                        .toList();

        List<BoardUserProgress> boardUserProgresses = new ArrayList<>();
        for(Long problemId : problemIds){
            boardUserProgresses.add(
                    new BoardUserProgress(boardId, userId, problemId)
            );
        }
        boardUserProgressRepository.saveAll(boardUserProgresses);
    }
}
