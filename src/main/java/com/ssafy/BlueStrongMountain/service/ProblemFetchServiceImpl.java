package com.ssafy.BlueStrongMountain.service;

import com.ssafy.BlueStrongMountain.dto.ProblemDto;
import com.ssafy.BlueStrongMountain.repository.ProblemRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class ProblemFetchServiceImpl implements ProblemFetchService{
    private final ProblemRepository problemRepository;
    //private final BoardRepository boardRepository;

    public ProblemFetchServiceImpl(
            //ProblemRepository problemRepository,
            //BoardRepository boardRepository
            ProblemRepository problemRepository
    ) {
        this.problemRepository = problemRepository;
        //this.boardRepository = boardRepository;
    }
    @Override
    public List<ProblemDto> fetchBaseProblems(Long groupId, String mode, Boolean unsolved) {
        return problemRepository.findAll();
    }

    private List<ProblemDto> fetchReviewProblems(Long groupId) {
        List<Long> problemIds = problemRepository.findReviewProblemIdsByGroup(groupId);
        return problemRepository.findByIdList(problemIds);
    }
}
