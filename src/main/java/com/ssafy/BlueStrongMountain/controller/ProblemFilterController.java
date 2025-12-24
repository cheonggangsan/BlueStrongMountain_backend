package com.ssafy.BlueStrongMountain.controller;

import com.ssafy.BlueStrongMountain.dto.ProblemDto;
import com.ssafy.BlueStrongMountain.dto.FilteredProblemsResponse;
import com.ssafy.BlueStrongMountain.dto.ProblemFilterCondition;
import com.ssafy.BlueStrongMountain.dto.ProblemFilterRequest;
import com.ssafy.BlueStrongMountain.service.ProblemDBFilterService;
import com.ssafy.BlueStrongMountain.service.ProblemFetchService;
import com.ssafy.BlueStrongMountain.service.ProblemFilterService;
import java.util.ArrayList;
import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/groups/{groupId}/problems")
@RequiredArgsConstructor
public class ProblemFilterController {
    private final ProblemFetchService fetchService;
    private final ProblemFilterService filterService;
    private final ProblemDBFilterService dbFilterService;


    @GetMapping("/filter")
    public ResponseEntity<FilteredProblemsResponse> filter(
            @PathVariable Long groupId,
            @RequestParam(required = false) String mode,
            @RequestParam(required = false) List<Long> problemIds,
            @RequestParam(required = false) Integer difficultyFrom,
            @RequestParam(required = false) Integer difficultyTo,
            @RequestParam(required = false) List<String> tags,
            @RequestParam(required = false) Integer minSolvers,
            @RequestParam(required = false) Boolean unsolved,
            @RequestParam(required = false) Integer option
    ) {
//test
        Long startTime = System.nanoTime();

        ProblemFilterRequest request = new ProblemFilterRequest(
                mode,
                problemIds,
                difficultyFrom,
                difficultyTo,
                tags,
                minSolvers,
                unsolved,
                option
        );
        System.out.println("test request DTO");
        System.out.println(request.toString());



        List<ProblemDto> base;
        List<ProblemDto> filtered;
        if(request.getMode().equals("review")){
            base = fetchService.fetchReviewProblems(groupId);
            filtered = filterService.applyFilters(groupId, base, request);
        }else{

            if(!request.getProblemIds().isEmpty()){
//                base = fetchService.fetchBaseProblems(groupId, request.getUnsolved());
//                filtered = filterService.applyFilters(groupId, base, request);
                filtered = dbFilterService.applyFilterFindByIds(request.getProblemIds());
            }else{
//                base = fetchService.fetchBaseProblems(groupId, request.getUnsolved());
//                filtered = filterService.applyFilters(groupId, base, request);
                filtered = dbFilterService.applyFilter(
                        groupId,
                        request.getUnsolved(),
                        request.getOption(),
                        new ProblemFilterCondition(
                                request.getDifficultyFrom(),
                                request.getDifficultyTo(),
                                request.getMinSolvers(),
                                request.getTags()
                        )
                );
            }
        }
//
//        System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
//        for(ProblemDto pd : base){
//            System.out.println(pd.toString());
//        }


//
        System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");//test
        System.out.println(request.toString());
        System.out.println("filtering problems start!");

        for(ProblemDto pd : filtered){
            System.out.println(pd.toString());
        }
        System.out.println("filtering problems end!");//test
        Long endTime = System.nanoTime();
        Long elapseTime = (endTime - startTime) / 1_000_000;
        //test
        System.out.println(elapseTime + "ms");


        return ResponseEntity.ok(new FilteredProblemsResponse(true, filtered));
    }
}
