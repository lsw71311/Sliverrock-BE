package com.example.silverrock.matching.controller;

import com.example.silverrock.matching.Service.MatchingService;
import com.example.silverrock.matching.dto.PostMatcingReq;
import com.example.silverrock.matching.repository.MatchingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.atomic.AtomicLong;

@RequiredArgsConstructor
@RestController
@RequestMapping("/matching")
public class MatchingController {

    @Autowired
    private MatchingRepository matchingRepository;
    private final MatchingService matchingService;

    private AtomicLong matchingIdGenerator = new AtomicLong(1); //매칭아이디 자동 생성 변수

    //매칭 요청
    @PostMapping("/{receiver}")
    public Long matchingRequest(@PathVariable("receiver") Long receiver, @RequestBody PostMatcingReq request) {
        Long matchingId = matchingIdGenerator.getAndIncrement(); // 순차적으로 매칭 아이디 생성

        this.matchingService.matchingRequest(request,matchingId);
        //이 메서드에서 생성한 매칭 아이디도 데이터에 따로 저장 해야하지 않나? 윗줄은 그냥 센더와 리시버의 아이디만 저장인디..

//        request.setMatchingId(matchingId); //매칭아이디 생성
//        request.setAccepted(false); // 요청 수신자가 수락하지 않은 상태로 초기화
//
//        matchingRepository.save(request); // 요청 데이터를 데이터베이스에 저장

        return matchingId; //고유번호 반환 //요청 완료 문구는???
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
