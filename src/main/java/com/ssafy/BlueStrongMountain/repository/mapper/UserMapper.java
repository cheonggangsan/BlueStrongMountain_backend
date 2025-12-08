package com.ssafy.BlueStrongMountain.repository.mapper;

import com.ssafy.BlueStrongMountain.domain.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserMapper {
    void insert(User user);

    void update(User user);

    User findById(@Param("id") Long id);

    User findByEmail(@Param("email") String email);

    User findByUsername(@Param("username") String username);

    void delete(@Param("id") Long id);
}
