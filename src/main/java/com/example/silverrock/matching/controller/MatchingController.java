package com.example.silverrock.matching.controller;

import com.example.silverrock.global.Response.BaseResponse;
import com.example.silverrock.global.Response.BaseResponseStatus;
import com.example.silverrock.matching.Service.MatchingService;
import com.example.silverrock.matching.dto.PostMatcingReq;
import com.example.silverrock.matching.repository.MatchingRequestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.atomic.AtomicLong;

@RequiredArgsConstructor
@RestController
@RequestMapping("/matching")
public class MatchingController {

    @Autowired
    private final MatchingRequestRepository matchingRequestRepository;
    private final MatchingService matchingService;

    private AtomicLong matchingIdGenerator = new AtomicLong(1); //매칭아이디 자동 생성 변수

    //매칭 요청
    @PostMapping("/{receiver}")
    public BaseResponse matchingRequest(@PathVariable("receiver") Long receiver) {
        Long matchingId = matchingService.matchingRequest(receiver);
//        return new BaseResponse<>(matchingService.matchingRequest(matchingId,postMatcingReq));
        return new BaseResponse<>(matchingId);
    }

    //매칭 수락
    // 매칭 수락
    @PostMapping("/accept/{matching_id}")
    public BaseResponse acceptMatching(@PathVariable("matching_id") Long matchingId) {
        matchingService.acceptMatching(matchingId);

        return new BaseResponse<>(BaseResponseStatus.SUCCESS);
    }


    //매칭 거절
    @DeleteMapping("/reject/{matching_id}")
    public BaseResponse rejectMatchingRequest(@PathVariable("matching_id") Long matchingId) {
        matchingService.rejectMatching(matchingId);
        return new BaseResponse<>(BaseResponseStatus.SUCCESS);

    }

}
