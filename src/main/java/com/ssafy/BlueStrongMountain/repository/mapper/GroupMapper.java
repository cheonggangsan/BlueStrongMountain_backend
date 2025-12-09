package com.ssafy.BlueStrongMountain.repository.mapper;

import com.ssafy.BlueStrongMountain.domain.Group;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Optional;

@Mapper
public interface GroupMapper {
    int insert(Group group);
    Optional<Group> findById(@Param("id") Long id);
    List<Group> findByIds(@Param("ids") List<Long> ids);
    List<Group> findByTitleLike(@Param("keyword") String keyword);
    int update(Group group);
    int delete(@Param("id") Long id);
}
