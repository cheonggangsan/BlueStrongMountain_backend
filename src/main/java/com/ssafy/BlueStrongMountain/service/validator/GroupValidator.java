package com.ssafy.BlueStrongMountain.service.validator;

import com.ssafy.BlueStrongMountain.dto.GroupCreateRequest;
import com.ssafy.BlueStrongMountain.dto.GroupUpdateRequest;
import com.ssafy.BlueStrongMountain.exception.InvalidGroupCreateException;
import com.ssafy.BlueStrongMountain.exception.InvalidGroupUpdateException;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.springframework.stereotype.Component;

@Component
public class GroupValidator {

    public void validateCreateRequest(final Long ownerId, final GroupCreateRequest request) {
        if (request == null) {
            throw new InvalidGroupCreateException("Request must not be null.");
        }

        if (request.getTitle() == null || request.getTitle().isBlank()) {
            throw new InvalidGroupCreateException("Group title must not be empty.");
        }

        validateDuplicateIds(ownerId, request.getManagerIds(), request.getMemberIds());
    }

    public void validateUpdateRequest(final GroupUpdateRequest request) {
        if (request == null) {
            throw new InvalidGroupUpdateException("Request must not be null.");
        }
    }

//    public void validateDuplicateMemberIds(final List<Long> memberIds) {
//        validateDuplicate(memberIds, "memberIds");
//    }
//
//    public void validateDuplicateManagerIds(final List<Long> managerIds) {
//        validateDuplicate(managerIds, "managerIds");
//    }

    private void validateDuplicateIds(
            final Long ownerId,
            final List<Long> managerIds,
            final List<Long> memberIds
    ) {
        final Set<Long> set = new HashSet<>();
        set.add(ownerId);

        if (managerIds != null) {
            for (Long id : managerIds) {
                if (set.contains(id)) {
                    throw new InvalidGroupCreateException("Duplicate id in managerIds and memberIds. id=" + id);
                }
                set.add(id);
            }
        }
        if (memberIds != null) {
            for (Long id : memberIds) {
                if (set.contains(id)) {
                    throw new InvalidGroupCreateException("Duplicate id in managerIds and memberIds. id=" + id);
                }
                set.add(id);
            }
        }
    }

//    private void validateDuplicate(final List<Long> ids, final String fieldName) {
//        if (ids == null) {
//            return;
//        }
//        final Set<Long> set = new HashSet<>();
//        for (Long id : ids) {
//            if (set.contains(id)) {
//                throw new InvalidGroupCreateException("Duplicate id in " + fieldName + ". id=" + id);
//            }
//            set.add(id);
//        }
//    }
}
