package com.ssafy.BlueStrongMountain.service;

import com.ssafy.BlueStrongMountain.domain.User;
import com.ssafy.BlueStrongMountain.domain.UserSolution;
import com.ssafy.BlueStrongMountain.dto.*;
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
    private static final String SOLVED_AC_USER_SEARCH_URL =
            "https://solved.ac/api/v3/search/user";
    private static final String SOLVED_AC_VERIFY_URL =
            "https://solved.ac/api/v3/account/verify_credentials";


    private static final int PAGE_SIZE = 50;

    private final UserRepository userRepository;
    private final UserSolutionRepository userSolutionRepository;
    private final WebClient webClient;

    @Override
    public void syncUserSolution(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found!"));

        //유저가 백준 아이디 없으면 반환 처리
        String handle = user.getBaekjoonHandle();
        if(handle == null || handle.isBlank()){
            return;
        }

        //백준 id로 solved 문제 가져오기
        List<Long> fetchedProblemIds = fetchSolvedProblemIds(handle);
        //새롭게 해결한 문제만 선별
        List<Long> newProblemIds = findNewSolvedProblemIds(userId, fetchedProblemIds);

        //현재 시간으로 해결 문제로 db에 추가
        LocalDateTime now = LocalDateTime.now();
        for(Long problemId : newProblemIds){
            userSolutionRepository.save(
                    UserSolution.create(userId, problemId, now)
            );
        }
    }

    @Override
    public boolean existSolvedAcUser(String handle) {
        SolvedAcUserSearchResponse solvedAcUserSearchResponse =
                webClient.get()
                        .uri(SOLVED_AC_USER_SEARCH_URL + "?query=" + handle)
                        .retrieve()
                        .bodyToMono(SolvedAcUserSearchResponse.class)
                        .block();

        if(solvedAcUserSearchResponse == null || solvedAcUserSearchResponse.getItems().isEmpty()){
            return false;
        }

        return solvedAcUserSearchResponse.getItems()
                .get(0)
                .getHandle().equals(handle);
    }

    @Override
    public boolean isSolvedAcVerified(String handle, String bio) {
        try {
            //BST-***** 예시 코드
            SolvedAcUserSearchResponse solvedAcUserSearchResponse =
                    webClient.get()
                            .uri(SOLVED_AC_USER_SEARCH_URL + "?query=" + handle)
                            .retrieve()
                            .bodyToMono(SolvedAcUserSearchResponse.class)
                            .block();



            if(solvedAcUserSearchResponse == null || solvedAcUserSearchResponse.getItems() == null)return false;
            return solvedAcUserSearchResponse.getItems().get(0).getBio().equals(bio);
        } catch (Exception e) {
            // 네트워크 오류, 5xx 등
            return false;
        }
    }



    @Override
    @Transactional(readOnly = true)
    public Set<Long> getSolvedProblemIds(Long userId) {
        return userSolutionRepository.findByUserId(userId)
                .stream()
                .map(UserSolution::getProblemId)
                .collect(Collectors.toSet());
    }

    private List<Long> findNewSolvedProblemIds(
            Long userId,
            List<Long> fetchedProblemIds
    ){
        Set<Long> existingSolvedProblemIds = getSolvedProblemIds(userId);

        return fetchedProblemIds.stream()
                .filter(id -> !existingSolvedProblemIds.contains(id))
                .toList();
    }

    private List<Long> fetchSolvedProblemIds(String handle){
        int page = 1;
        int totalCount = Integer.MAX_VALUE;

        List<Long> solvedProblemIds = new ArrayList<>();


        //true로 설정해도 문제 없을 듯?
        while((page - 1) * PAGE_SIZE < totalCount) {
            //final 변수여야 webClient uri에 추가할 수 있다.
            int finalPage = page;
            SolvedAcResponse response = webClient.get()
                    .uri(SOLVED_AC_URL + "?query=s@" + handle + "&page=" + finalPage)
                    .retrieve()
                    .bodyToMono(SolvedAcResponse.class)
                    .block();

            if(response == null || response.getItems().isEmpty()){
                break;
            }

            for(SolvedAcProblem item : response.getItems()){
                solvedProblemIds.add(item.getProblemId());
            }
            page++;
        }

        return solvedProblemIds;
    }
}
