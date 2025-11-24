package com.ssafy.BlueStrongMountain.controller;

import com.ssafy.BlueStrongMountain.dto.ProblemDto;
import com.ssafy.BlueStrongMountain.dto.FilteredProblemsResponse;
import com.ssafy.BlueStrongMountain.dto.ProblemFilterRequest;
import com.ssafy.BlueStrongMountain.service.ProblemFetchService;
import com.ssafy.BlueStrongMountain.service.ProblemFilterService;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
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

    @PostMapping("/filter")
    public ResponseEntity<FilteredProblemsResponse> filter(
            @PathVariable Long groupId,
            @RequestBody ProblemFilterRequest request
    ) {

        String mode = request.getMode();
        List<ProblemDto> base;
        if(mode.equals("review")){
            base = fetchService.fetchReviewProblems(groupId);
            System.out.println("this is review ?????????????????????????????????");

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
//        System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
//        System.out.println(request.toString());
//
//        for(ProblemDto pd : filtered){
//            System.out.println(pd.toString());
//        }


        return ResponseEntity.ok(new FilteredProblemsResponse(true, filtered));
    }
}
