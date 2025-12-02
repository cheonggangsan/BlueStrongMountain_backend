package com.ssafy.BlueStrongMountain.service;

import com.ssafy.BlueStrongMountain.domain.GroupRole;
import com.ssafy.BlueStrongMountain.domain.UserGroup;
import com.ssafy.BlueStrongMountain.exception.CannotRemoveOwnerException;
import com.ssafy.BlueStrongMountain.exception.UserNotInGroupException;
import com.ssafy.BlueStrongMountain.repository.UserGroupRepository;
import com.ssafy.BlueStrongMountain.service.validator.GroupAuthorityService;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class GroupMemberServiceImpl implements GroupMemberService {

    private final UserGroupRepository userGroupRepository;
    private final GroupAuthorityService groupAuthorityService;

    public GroupMemberServiceImpl(
            final UserGroupRepository userGroupRepository,
            final GroupAuthorityService groupAuthorityService
    ) {
        this.userGroupRepository = userGroupRepository;
        this.groupAuthorityService = groupAuthorityService;
    }

    @Override
    public void addManagers(
            final Long requesterId,
            final Long groupId,
            final List<Long> managerIds
    ) {
        groupAuthorityService.validateOwner(requesterId, groupId);

        for(Long managerId : managerIds){
            final UserGroup target = userGroupRepository.findByUserIdAndGroupId(managerId, groupId)
                    .orElseThrow(() -> new UserNotInGroupException(managerId, groupId));

            if (target.getRole() == GroupRole.MANAGER) {
                //throw new AlreadyManagerException(userId, groupId);
            }

            if (target.getRole() == GroupRole.OWNER) {
                throw new CannotRemoveOwnerException(managerId, groupId);
            }

            target.changeRole(GroupRole.MANAGER);
            userGroupRepository.save(target);
        }
    }

    @Override
    public void removeManagers(
            final Long requesterId,
            final Long groupId,
            final List<Long> managerIds
    ) {
        groupAuthorityService.validateOwner(requesterId, groupId);

        for (Long managerId : managerIds) {
            if (managerId.equals(requesterId)) {
                throw new CannotRemoveOwnerException(managerId, groupId);
            }

            final UserGroup userGroup = userGroupRepository.findByUserIdAndGroupId(managerId, groupId)
                    .orElseThrow(() -> new UserNotInGroupException(managerId, groupId));

            if (userGroup.getRole() == GroupRole.OWNER) {
                throw new CannotRemoveOwnerException(managerId, groupId);
            }

            userGroup.changeRole(GroupRole.MEMBER);
            userGroupRepository.save(userGroup);
        }
    }

    @Override
    public void addMembers(
            final Long requesterId,
            final Long groupId,
            final List<Long> userIds
    ) {
        groupAuthorityService.validateManager(requesterId, groupId);

        for(Long userId : userIds){
            final boolean exists = userGroupRepository.findByUserIdAndGroupId(userId, groupId).isPresent();
            if (exists) {
                //중복 사용자 발생 예외 추가
                continue;
            }

            final LocalDateTime now = LocalDateTime.now();
            final UserGroup userGroup = UserGroup.create(userId, groupId, GroupRole.MEMBER, now);
            userGroupRepository.save(userGroup);
        }
    }

    @Override
    public void removeMembers(
            final Long requesterId,
            final Long groupId,
            final List<Long> userIds
    ) {
        groupAuthorityService.validateOwner(requesterId, groupId);

        for(Long userId : userIds){
            if (userId.equals(requesterId)) {
                throw new CannotRemoveOwnerException(userId, groupId);
            }

            final UserGroup userGroup = userGroupRepository.findByUserIdAndGroupId(userId, groupId)
                    .orElseThrow(() -> new UserNotInGroupException(userId, groupId));

            if (userGroup.getRole() == GroupRole.OWNER) {
                throw new CannotRemoveOwnerException(userId, groupId);
            }

            userGroupRepository.deleteByUserIdAndGroupId(userId, groupId);
        }
    }

    @Override
    public void leaveGroup(
            final Long requesterId,
            final Long groupId
    ) {
        final UserGroup userGroup = userGroupRepository.findByUserIdAndGroupId(requesterId, groupId)
                .orElseThrow(() -> new UserNotInGroupException(requesterId, groupId));

        if (userGroup.getRole() == GroupRole.OWNER) {
            throw new CannotRemoveOwnerException(requesterId, groupId);
        }

        userGroupRepository.deleteByUserIdAndGroupId(requesterId, groupId);
    }
}
