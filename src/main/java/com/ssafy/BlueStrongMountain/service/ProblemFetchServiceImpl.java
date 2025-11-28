package com.ssafy.BlueStrongMountain.service;

import com.ssafy.BlueStrongMountain.dto.ProblemDto;
import com.ssafy.BlueStrongMountain.repository.GroupRepository;
import com.ssafy.BlueStrongMountain.repository.ProblemRepository;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class ProblemFetchServiceImpl implements ProblemFetchService{
    private final ProblemRepository problemRepository;
    //private final BoardRepository boardRepository;
    private final GroupRepository groupRepository;

    public ProblemFetchServiceImpl(
            //ProblemRepository problemRepository,
            //BoardRepository boardRepository
            ProblemRepository problemRepository,
            GroupRepository groupRepository
    ) {
        this.problemRepository = problemRepository;
        this.groupRepository = groupRepository;
        //this.boardRepository = boardRepository;
    }
    @Override
    public List<ProblemDto> fetchBaseProblems(Long groupId, Boolean unsolved) {
        if (Boolean.TRUE.equals(unsolved)) {
            // 그룹에서 해결된 문제 ID 가져오기
            //List<Long> solvedProblemIds = groupRepository.findGroupSolvedProblems(groupId);
            // 모든 문제를 가져옴
            List<ProblemDto> allProblems = problemRepository.findAll();

            List<Long> solvedProblemIds = groupRepository.findUsersSolvedProblems(groupId);

            System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
            System.out.println("test unsolved problems");
            for(Long ele : solvedProblemIds){
                System.out.println(ele); //test
            }

            List<ProblemDto> testDto =  allProblems.stream()
                    .filter(problem -> !solvedProblemIds.contains(problem.getId()))  // 해결되지 않은 문제만
                    .collect(Collectors.toList());

            for(ProblemDto ele : testDto){
                System.out.println(ele.toString());
            }
            //test
            return testDto;
        }
        return problemRepository.findAll();
    }

    @Override
    public List<ProblemDto> fetchReviewProblems(Long groupId) {
        List<Long> problemIds = groupRepository.findGroupSolvedProblems(groupId);
        return problemRepository.findByIdList(problemIds);
    }
}
