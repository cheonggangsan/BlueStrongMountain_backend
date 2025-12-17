package com.ssafy.BlueStrongMountain.scheduler;

import com.ssafy.BlueStrongMountain.domain.User;
import com.ssafy.BlueStrongMountain.exception.UserNotFoundException;
import com.ssafy.BlueStrongMountain.repository.UserRepository;
import com.ssafy.BlueStrongMountain.service.SolvedAcSyncService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class SolvedAcScheduler {
    private final UserRepository userRepository;
    private final SolvedAcSyncService solvedAcSyncService;

    //10분에 한번씩 반복
    @Scheduled(fixedDelay = 30 * 1000)
    public void syncAllUsersSolvedProblems(){
        User initUser = userRepository.findById(1L)
                .orElseThrow(UserNotFoundException::new);

//        long startTime = System.nanoTime();//test
//
//        List<Long> findNewProblemIds =
//                solvedAcSyncService.testFetchProblemIds(initUser.getId());


        solvedAcSyncService.syncUserSolution(initUser.getId());

//        long endTime = System.nanoTime();//test
//        long elapsedMs = (endTime - startTime) / 1_000_000;

//        System.out.println("new solved problem start !!");
//        for(Long id : findNewProblemIds){
//            System.out.println(id);
//        }
//        System.out.println("new solved problem end !!");

//        System.out.println("test solved ac sync Time = " + elapsedMs + "ms");//test
//
    }
}
