package com.ssafy.BlueStrongMountain.repository;

import com.ssafy.BlueStrongMountain.domain.Group;
import com.ssafy.BlueStrongMountain.repository.mapper.GroupMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class GroupRepositoryImpl implements GroupRepository{
    private final GroupMapper mapper;

    @Override
    public Group save(Group group) {
        if (group.getId() == null) {
            mapper.insert(group);
            return group;
        }
        mapper.update(group);
        return group;
    }

    @Override
    public Optional<Group> findById(Long groupId) {
        return mapper.findById(groupId);
    }

    @Override
    public List<Group> findByIds(List<Long> groupIds) {
        return mapper.findByIds(groupIds);
    }

    @Override
    public List<Group> findByTitleLike(String titleKeyword) {
        return mapper.findByTitleLike(titleKeyword);
    }

    @Override
    public void deleteById(Long groupId) {
        mapper.delete(groupId);
    }
}
