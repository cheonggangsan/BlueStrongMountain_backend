//package com.ssafy.BlueStrongMountain.repository;
//
//import com.ssafy.BlueStrongMountain.domain.GroupRole;
//import com.ssafy.BlueStrongMountain.domain.UserGroup;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Map;
//import java.util.Optional;
//import java.util.concurrent.ConcurrentHashMap;
//import org.springframework.stereotype.Repository;
//
////@Repository
//public class InMemoryUserGroupRepository implements UserGroupRepository{
//
//    // Key: groupId
//    private final Map<Long, List<UserGroup>> groups = new ConcurrentHashMap<>();
//
//    // Key: userId
//    private final Map<Long, List<UserGroup>> users = new ConcurrentHashMap<>();
//
//    @Override
//    public UserGroup save(UserGroup userGroup) {
////        groups.computeIfAbsent(userGroup.getGroupId(), k -> new ArrayList<>()).add(userGroup);
////        users.computeIfAbsent(userGroup.getUserId(), k -> new ArrayList<>()).add(userGroup);
//
//        Long userId = userGroup.getUserId();
//        Long groupId = userGroup.getGroupId();
//
//        List<UserGroup> groupList = groups.getOrDefault(groupId, new ArrayList<>());
//        groupList.removeIf(ug -> ug.getUserId().equals(userId));
//
//        List<UserGroup> userList = users.getOrDefault(userId, new ArrayList<>());
//        userList.removeIf(ug -> ug.getGroupId().equals(groupId));
//
//        groupList.add(userGroup);
//        userList.add(userGroup);
//
//        groups.put(groupId, groupList);
//        users.put(userId, userList);
//
////        //test
////
////        List<UserGroup> members = findByGroupIdAndRole(groupId, GroupRole.MEMBER);
////        List<UserGroup> managers = findByGroupIdAndRole(groupId, GroupRole.MANAGER);
////
////        System.out.println("get members!!!!!!!!!!!1");
////        for(UserGroup ug : members)
////            System.out.println(ug.getUserId());
////
////        System.out.println("get managers!!!!!!!!!!!!");
////        for(UserGroup ug : managers){
////            System.out.println(ug.getUserId());
////        }
////
////        //test
//
//        return userGroup;
//    }
//
//    //user
//    @Override
//    public List<UserGroup> findByUserId(Long userId) {
//        return users.getOrDefault(userId, List.of());
//    }
//
//    @Override
//    public List<UserGroup> findByGroupId(Long groupId) {
//        return groups.getOrDefault(groupId, List.of());
//    }
//
//    @Override
//    public Optional<UserGroup> findByUserIdAndGroupId(Long userId, Long groupId) {
//        List<UserGroup> list = groups.get(groupId);
//
//        if (list == null) {
//            return Optional.empty();
//        }
//
//        for (UserGroup ug : list) {
//            if (ug.getUserId().equals(userId)) {
//                return Optional.of(ug);
//            }
//        }
//
//        return Optional.empty();
//    }
//
//    @Override
//    public void deleteByUserIdAndGroupId(Long userId, Long groupId) {
//        List<UserGroup> list = groups.get(groupId);
//        if (list != null) {
//            list.removeIf(ug -> ug.getUserId().equals(userId));
//        }
//
//        List<UserGroup> userList = users.get(userId);
//        if (userList != null) {
//            userList.removeIf(ug -> ug.getGroupId().equals(groupId));
//        }
//    }
//
//    @Override
//    public void deleteByGroupIdAndUserIdIn(Long groupId, List<Long> userIds) {
//        List<UserGroup> list = groups.get(groupId);
//
//        if (list != null) {
//            list.removeIf(ug -> userIds.contains(ug.getUserId()));
//        }
//
//        for (Long userId : userIds) {
//            List<UserGroup> userList = users.get(userId);
//            if (userList != null) {
//                userList.removeIf(ug -> ug.getGroupId().equals(groupId));
//            }
//        }
//    }
//
//    @Override
//    public int countByGroupId(Long groupId) {
//        return findByGroupIdAndRole(groupId, GroupRole.MEMBER).size();
//    }
//
//    @Override
//    public List<UserGroup> findByGroupIdAndRole(Long groupId, GroupRole role) {
//        List<UserGroup> result = new ArrayList<>();
//        List<UserGroup> list = groups.get(groupId);
//
//        if (list == null) {
//            return result;
//        }
//
//        for (UserGroup ug : list) {
//            if (ug.getRole() == role) {
//                result.add(ug);
//            }
//        }
//
//        return result;
//    }
//}
