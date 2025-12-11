package com.ssafy.BlueStrongMountain.service;

import com.ssafy.BlueStrongMountain.dto.GroupCreateRequest;
import com.ssafy.BlueStrongMountain.dto.GroupDetailDto;
import com.ssafy.BlueStrongMountain.dto.GroupSummaryDto;
import com.ssafy.BlueStrongMountain.dto.GroupUpdateRequest;
import java.util.List;

public interface GroupService {
    Long createGroup(Long ownerId, GroupCreateRequest request);
    GroupDetailDto getGroupDetail(Long requesterId, Long groupId);
    List<GroupSummaryDto> findMyGroups(Long requesterId);
    List<GroupSummaryDto> searchMyGroups(Long requesterId, String name);
    void updateGroup(Long requesterId, Long groupId, GroupUpdateRequest request);
    void changeOwner(Long requesterId, Long groupId, Long newOwnerId);
}