package com.ssafy.BlueStrongMountain.service.validator;

import com.ssafy.BlueStrongMountain.domain.GroupRole;
import com.ssafy.BlueStrongMountain.domain.UserGroup;
import com.ssafy.BlueStrongMountain.exception.CannotTransferOwnershipException;
import com.ssafy.BlueStrongMountain.exception.OwnerPermissionRequiredException;
import com.ssafy.BlueStrongMountain.exception.UserNotInGroupException;
import com.ssafy.BlueStrongMountain.repository.UserGroupRepository;
import org.springframework.stereotype.Component;

@Component
public class GroupAuthorityServiceImpl implements GroupAuthorityService {

    private final UserGroupRepository userGroupRepository;

    public GroupAuthorityServiceImpl(final UserGroupRepository userGroupRepository) {
        this.userGroupRepository = userGroupRepository;
    }

    @Override
    public void validateOwner(final Long userId, final Long groupId) {
        final UserGroup userGroup = getUserGroupOrThrow(userId, groupId);

        if (userGroup.getRole() != GroupRole.OWNER) {
            throw new OwnerPermissionRequiredException();
        }
    }

    @Override
    public void validateManager(final Long userId, final Long groupId) {
        final UserGroup userGroup = getUserGroupOrThrow(userId, groupId);

        if (userGroup.getRole() == GroupRole.MEMBER) {
            throw new OwnerPermissionRequiredException();
        }
    }

    @Override
    public void validateGroupMember(final Long userId, final Long groupId) {
        getUserGroupOrThrow(userId, groupId);
    }

    @Override
    public void validateNotOwner(final Long userId, final Long groupId) {
//        final UserGroup userGroup = getUserGroupOrThrow(userId, groupId);
//
//        if (userGroup.getRole() == GroupRole.OWNER) {
//            throw new CannotTransferOwnershipException("Owner cannot perform this action.");
//        }
    }

    @Override
    public void validateUserInGroup(final Long userId, final Long groupId) {
        getUserGroupOrThrow(userId, groupId);
    }

    @Override
    public void validateOwnerTransferable(
            final Long requesterId,
            final Long groupId,
            final Long newOwnerId
    ) {
        validateOwner(requesterId, groupId);

        if (requesterId.equals(newOwnerId)) {
            throw new CannotTransferOwnershipException("Owner cannot transfer ownership to self.");
        }

        getUserGroupOrThrow(newOwnerId, groupId);
    }

    private UserGroup getUserGroupOrThrow(final Long userId, final Long groupId) {
        return userGroupRepository.findByUserIdAndGroupId(userId, groupId)
                .orElseThrow(() -> new UserNotInGroupException(userId, groupId));
    }
}
