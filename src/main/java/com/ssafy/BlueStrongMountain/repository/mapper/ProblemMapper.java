package com.ssafy.BlueStrongMountain.repository.mapper;

import com.ssafy.BlueStrongMountain.domain.Problem;
import com.ssafy.BlueStrongMountain.dto.ProblemDto;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Optional;

public interface ProblemMapper {
    List<ProblemDto> findAll();
    List<ProblemDto> findByIdList(@Param("ids") List<Long> ids);
//    Optional<Problem> findById(Long id);
//    List<Problem> findByTag(Long tagId);
//    void save(Problem problem);
//    void update(Problem problem);

}
