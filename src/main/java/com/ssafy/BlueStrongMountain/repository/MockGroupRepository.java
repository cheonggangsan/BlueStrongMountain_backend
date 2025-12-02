package com.ssafy.BlueStrongMountain.repository;

import com.ssafy.BlueStrongMountain.domain.Group;
import com.ssafy.BlueStrongMountain.dto.GroupProblemDto;
import com.ssafy.BlueStrongMountain.dto.UserSolvedDto;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;
import org.springframework.stereotype.Repository;

@Repository
public class MockGroupRepository implements GroupRepository{
    private static final List<GroupProblemDto> MOCK_GROUP_SOLVED_PROBLEMS = List.of(
            new GroupProblemDto(1L, 1003L, 1),
            new GroupProblemDto(1L, 1005L, 2),
            new GroupProblemDto(1L, 1009L, 3)
    );
    private static final List<UserSolvedDto> MOCK_EACH_USERS_SOLVED_PROBLEMS = List.of(
            new UserSolvedDto(1L, 1003L, 1L),
            new UserSolvedDto(2L, 1003L, 1L),
            new UserSolvedDto(1L, 1005L, 1L),
            new UserSolvedDto(1L, 1409L, 1L),
            new UserSolvedDto(2L, 2004L, 1L)
    );


    @Override
    public List<Long> findUserIdsByGroupId(Long groupId) {
        return List.of(1L, 2L);
    }

    @Override
    public List<Long> findGroupSolvedProblems(Long groupId) {
        return MOCK_GROUP_SOLVED_PROBLEMS.stream()
                .map(GroupProblemDto::getProblemId)
                .collect(Collectors.toList());
    }

    @Override
    public List<Long> findUsersSolvedProblems(Long groupId) {
        return MOCK_EACH_USERS_SOLVED_PROBLEMS.stream()
                .map(UserSolvedDto::getProblemId)
                .distinct()
                .collect(Collectors.toList());
    }
    private final Map<Long, Group> store = new ConcurrentHashMap<>();
    private final AtomicLong sequence = new AtomicLong(1);


    @Override
    public Group save(Group group) {
        if (group.getId() == null) {
            Long id = sequence.getAndIncrement();
            setId(group, id);
        }

        store.put(group.getId(), group);
        return group;
    }

    @Override
    public Optional<Group> findById(final Long groupId) {
        return Optional.ofNullable(store.get(groupId));
    }

    // 안필요할 수도?
    @Override
    public List<Group> findByIds(final List<Long> groupIds) {
        List<Group> result = new ArrayList<>();

        for (Long id : groupIds) {
            if (store.containsKey(id)) {
                result.add(store.get(id));
            }
        }
        return result;
    }

    @Override
    public List<Group> findByTitleLike(String titleKeyword) {
        List<Group> result = new ArrayList<>();

        for (Group group : store.values()) {
            if (group.getTitle().toLowerCase().contains(titleKeyword.toLowerCase())) {
                result.add(group);
            }
        }
        return result;
    }

    @Override
    public void deleteById(Long groupId) {
        store.remove(groupId);
    }
    private void setId(final Group group, final Long id) {
        try {
            var field = Group.class.getDeclaredField("id");
            field.setAccessible(true);
            field.set(group, id);
        } catch (Exception e) {
            throw new RuntimeException("Reflection error: cannot set ID", e);
        }
    }

}
