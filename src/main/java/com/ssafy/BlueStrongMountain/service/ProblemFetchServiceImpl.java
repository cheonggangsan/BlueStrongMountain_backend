package com.ssafy.BlueStrongMountain.service;

import com.ssafy.BlueStrongMountain.domain.Board;
import com.ssafy.BlueStrongMountain.domain.BoardProblem;
import com.ssafy.BlueStrongMountain.domain.UserGroup;
import com.ssafy.BlueStrongMountain.domain.UserSolution;
import com.ssafy.BlueStrongMountain.dto.ProblemDto;
import com.ssafy.BlueStrongMountain.repository.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProblemFetchServiceImpl implements ProblemFetchService{
    private final ProblemRepository problemRepository;
    private final BoardRepository boardRepository;
    private final BoardProblemRepository boardProblemRepository;
    private final UserGroupRepository userGroupRepository;
    private final UserSolutionRepository userSolutionRepository;


    @Override
    public List<ProblemDto> fetchBaseProblems(Long groupId, Boolean unsolved) {
        if (Boolean.TRUE.equals(unsolved)) {
            // 그룹에서 해결된 문제 ID 가져오기
            //List<Long> solvedProblemIds = groupRepository.findGroupSolvedProblems(groupId);
            // 모든 문제를 가져옴
            List<ProblemDto> allProblems = problemRepository.findAll();

            //TODO mock 제거
            List<Long> solvedProblemIds = extractDistinctSolvedProblemIds(groupId);

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
        List<Board> boards = boardRepository.findByGroupId(groupId);

        Set<Long> problemIds = new HashSet<>();

        for (Board board : boards) {
            List<BoardProblem> bps = boardProblemRepository.findByBoardId(board.getId());
            for (BoardProblem bp : bps) {
                problemIds.add(bp.getProblemId()); // 중복 자동 제거
            }
        }
        return problemRepository.findByIdList(new ArrayList<>(problemIds));
    }
//    private List<Long> findUsersSolvedProblems() {
//        return MOCK_EACH_USERS_SOLVED_PROBLEMS.stream()
//                .map(UserSolvedDto::getProblemId)
//                .distinct()
//                .collect(Collectors.toList());
//    }
    private List<Long> extractDistinctSolvedProblemIds(Long groupId){
        List<UserGroup> userGroups = userGroupRepository.findByGroupId(groupId);

        Set<Long> problemIds = new HashSet<>();
        for(UserGroup ug : userGroups){
            Long userId = ug.getUserId();
            List<UserSolution> solutions = userSolutionRepository.findByUserId(userId);

            for(UserSolution s : solutions){
                problemIds.add(s.getProblemId());
            }
        }
        return new ArrayList<>(problemIds);
    }
}
