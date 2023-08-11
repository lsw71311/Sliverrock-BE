package com.example.silverrock.matching.controller;

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
    public Long matchingRequest(@PathVariable("receiver") Long receiver, @RequestBody PostMatcingReq request) {
        Long matchingId = matchingService.matchingRequest(request);

        return matchingId;
    }

    //매칭 수락
    // 매칭 수락
    @PostMapping("/accept/{matching_id}")
    public String acceptMatching(@PathVariable("matching_id") Long matchingId) {
        matchingService.acceptMatching(matchingId);

        return "'ㅇㅇㅇ'님이 친구 요청을 수락하셨습니다.";
    }


    //매칭 거절
    @DeleteMapping("/reject/{matching_id}")
    public String rejectMatchingRequest(@PathVariable("matching_id") Long matchingId) {
        matchingService.rejectMatching(matchingId);

        return "Matching request rejected and deleted.";
    }

}
