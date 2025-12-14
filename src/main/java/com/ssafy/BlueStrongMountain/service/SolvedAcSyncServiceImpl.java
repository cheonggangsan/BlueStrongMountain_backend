package com.ssafy.BlueStrongMountain.service;

import com.ssafy.BlueStrongMountain.domain.User;
import com.ssafy.BlueStrongMountain.domain.UserSolution;
import com.ssafy.BlueStrongMountain.dto.SolvedAcProblem;
import com.ssafy.BlueStrongMountain.dto.SolvedAcResponse;
import com.ssafy.BlueStrongMountain.repository.UserRepository;
import com.ssafy.BlueStrongMountain.repository.UserSolutionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Transactional
public class SolvedAcSyncServiceImpl implements SolvedAcSyncService{
    private static final String SOLVED_AC_URL =
            "https://solved.ac/api/v3/search/problem";

    private static final int PAGE_SIZE = 50;

    private final UserRepository userRepository;
    private final UserSolutionRepository userSolutionRepository;
    private final WebClient webClient;

    @Override
    public void syncUserSolution(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found!"));

        String handle = user.getBaekjoonHandle();
        if(handle == null || handle.isBlank()){
            return;
        }

        int page = 1;
        int totalCount = Integer.MAX_VALUE;

        Set<Long> existingSolvedProblemIds = getSolvedProblemIds(userId);


        while((page - 1) * PAGE_SIZE < totalCount) {
            int finalPage = page;
            SolvedAcResponse response = webClient.get()
                    .uri(SOLVED_AC_URL + "?query=s@" + handle + "&page=" + finalPage)
                    .retrieve()
                    .bodyToMono(SolvedAcResponse.class)
                    .block();

            if(response == null || response.getItems().isEmpty()){
                break;
            }
            totalCount = response.getCount();
            for(SolvedAcProblem item : response.getItems()){
                Long problemId = item.getProblemId();



                if(existingSolvedProblemIds.contains(problemId)){
                    continue;
                }

                //DB에 userSolution 저장 로직
                userSolutionRepository.save(
                        UserSolution.create(
                                userId,
                                problemId,
                                LocalDateTime.now()
                        )
                );
            }
            page++;
        }

//        System.out.println(testProblemIds.size());
//        for(Long pid : testProblemIds){
//            System.out.print(pid + " ");
//        }
//        System.out.println();
//
//

    }

    @Override
    @Transactional(readOnly = true)
    public Set<Long> getSolvedProblemIds(Long userId) {
        return userSolutionRepository.findByUserId(userId)
                .stream()
                .map(UserSolution::getProblemId)
                .collect(Collectors.toSet());
    }
}
