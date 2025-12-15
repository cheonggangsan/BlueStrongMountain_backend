package com.ssafy.BlueStrongMountain.repository.mapper;

import com.ssafy.BlueStrongMountain.domain.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UserMapper {
    void insert(User user);

    void update(User user);

    User findById(@Param("id") Long id);

    User findByEmail(@Param("email") String email);

    User findByUsername(@Param("username") String username);

    List<User> searchByEmail(@Param("keyword") String keyword);

    List<User> searchByUsername(@Param("keyword") String keyword);

    void delete(@Param("id") Long id);
}
