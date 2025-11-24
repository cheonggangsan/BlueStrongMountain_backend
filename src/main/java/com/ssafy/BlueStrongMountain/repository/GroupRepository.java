package com.ssafy.BlueStrongMountain.repository;

import java.util.List;

public interface GroupRepository {
    List<Long> findUserIdsByGroupId(Long groupId);
}
