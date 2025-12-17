package com.ssafy.BlueStrongMountain.scheduler;

import com.ssafy.BlueStrongMountain.domain.User;
import com.ssafy.BlueStrongMountain.repository.UserRepository;
import com.ssafy.BlueStrongMountain.service.SolvedAcSyncService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class SolvedAcScheduler {
    private final UserRepository userRepository;
    private final SolvedAcSyncService solvedAcSyncService;

    //30초에 한번씩 반복
    @Scheduled(fixedDelay = 30 * 1000)
    public void syncAllUsersSolvedProblems(){


        List<Long> userIds = userRepository.findAll()
                .stream()
                .map(User::getId)
                .toList();

        for(Long userId : userIds){
            //FIXME handle을 전달하는게 더 바람직하지 않나?????
            solvedAcSyncService.syncUserSolution(userId);
        }

//        User initUser = userRepository.findById(1L)
//                .orElseThrow(UserNotFoundException::new);

//        long startTime = System.nanoTime();//test
//
//        List<Long> findNewProblemIds =
//                solvedAcSyncService.testFetchProblemIds(initUser.getId());

//
//        solvedAcSyncService.syncUserSolution(initUser.getId());

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
