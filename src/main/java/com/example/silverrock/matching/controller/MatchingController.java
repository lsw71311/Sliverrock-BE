package com.example.silverrock.matching.controller;//package com.example.silverrock.matching.controller;
//
//import com.example.silverrock.matching.dto.PostMatcingReq;
//import lombok.RequiredArgsConstructor;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.concurrent.atomic.AtomicLong;
//
//@RequiredArgsConstructor
//@RestController
//@RequestMapping("/matching")
//public class MatchingController {
//
//    @Autowired
//    private MatchingRequestRepository matchingRequestRepository;
//
//    private AtomicLong matchingIdGenerator = new AtomicLong(1);
//
//    //매칭 요청
//    @PostMapping("/{receiver}")
//    public String sendMatchingRequest(@PathVariable("receiver") Long receiver, @RequestBody PostMatcingReq request) {
//        Long matchingId = matchingIdGenerator.getAndIncrement(); // 순차적으로 매칭 아이디 생성
//
//        request.setMatchingId(matchingId); //매칭아이디 생성
//        request.setAccepted(false); // 요청 수신자가 수락하지 않은 상태로 초기화
//
//        matchingRequestRepository.save(request); // 요청 데이터를 데이터베이스에 저장
//
//        return "매칭 요청 성공!"; //고유번호 반환
//    }
//
//    //매칭 수락
//    @PostMapping("/accept/{matching_id}")
//    public String acceptMatchingRequest(@RequestBody MatchingRequest request) {
//        MatchingRequest existingRequest = matchingRequestRepository.findById(request.getId()).orElse(null);
//
//        if (existingRequest != null) {
//            existingRequest.setAccepted(true); // 요청 수락 처리
//            matchingRequestRepository.save(existingRequest); // 변경된 상태를 데이터베이스에 저장
//            return "'ㅇㅇㅇ'님이 친구 요청을 수락하셨습니다.";
//        } else {
//            return "Matching request not found."; // 요청을 수락하지 않고 방치하면?
//        }
//    }
//
//    //매칭 거절
//    @DeleteMapping("/reject/{matching_id}")
//    public String rejectMatchingRequest(@RequestBody MatchingRequest request) {
//        MatchingRequest existingRequest = matchingRequestRepository.findById(request.getId()).orElse(null);
//
//        if (existingRequest != null) {
//            matchingRequestRepository.delete(existingRequest); // 요청 거절 후 데이터 삭제
////            return "Matching request rejected and deleted."; //요청 거절하면 아무말도 없이..?
//        } else {
//            return "Matching request not found.";
//        }
//    }
//}
