package com.ssafy.BlueStrongMountain.controller;

import com.ssafy.BlueStrongMountain.dto.ProblemDto;
import com.ssafy.BlueStrongMountain.dto.FilteredProblemsResponse;
import com.ssafy.BlueStrongMountain.dto.ProblemFilterRequest;
import com.ssafy.BlueStrongMountain.service.ProblemFetchService;
import com.ssafy.BlueStrongMountain.service.ProblemFilterService;
import java.util.ArrayList;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/groups/{groupId}/problems")
public class ProblemFilterController {
    private final ProblemFetchService fetchService;
    private final ProblemFilterService filterService;

    public ProblemFilterController(
            ProblemFetchService fetchService,
            ProblemFilterService filterService
    ) {
        this.fetchService = fetchService;
        this.filterService = filterService;
    }

    @GetMapping("/filter")
    public ResponseEntity<FilteredProblemsResponse> filter(
            @PathVariable Long groupId,
            @RequestParam(required = false) String mode,
            @RequestParam(required = false) List<Long> problemIds,
            @RequestParam(required = false) Integer difficultyFrom,
            @RequestParam(required = false) Integer difficultyTo,
            @RequestParam(required = false) List<String> tags,
            @RequestParam(required = false) Integer minSolvers,
            @RequestParam(required = false) Boolean unsolved
    ) {

        ProblemFilterRequest request = new ProblemFilterRequest(
                mode,
                problemIds,
                difficultyFrom,
                difficultyTo,
                tags,
                minSolvers,
                unsolved
        );
        System.out.println("test request DTO");
        System.out.println(request.toString());



        List<ProblemDto> base;
        if(request.getMode().equals("review")){
            base = fetchService.fetchReviewProblems(groupId);

        }else{
            base = fetchService.fetchBaseProblems(groupId, request.getUnsolved());
        }
//
//        System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
//        for(ProblemDto pd : base){
//            System.out.println(pd.toString());
//        }


        List<ProblemDto> filtered = filterService.applyFilters(base, request);

//
        System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");//test
        System.out.println(request.toString());
        System.out.println("filtering problems start!");

        for(ProblemDto pd : filtered){
            System.out.println(pd.toString());
        }
        System.out.println("filtering problems end!");//test


        return ResponseEntity.ok(new FilteredProblemsResponse(true, filtered));
    }
}
