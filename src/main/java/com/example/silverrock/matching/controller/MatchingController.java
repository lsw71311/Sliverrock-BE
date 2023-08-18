package com.example.silverrock.matching.controller;

import com.example.silverrock.global.Response.BaseException;
import com.example.silverrock.global.Response.BaseResponse;
import com.example.silverrock.global.Response.BaseResponseStatus;
import com.example.silverrock.login.jwt.JwtService;
import com.example.silverrock.matching.Service.MatchingService;
import com.example.silverrock.matching.dto.PostMatcingReq;
import com.example.silverrock.matching.repository.MatchingRequestRepository;
import com.example.silverrock.user.dto.GetNearUserRes;
import com.example.silverrock.user.dto.GetUserRes;
import com.example.silverrock.user.dto.ReceivedMatchingRes;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@RequiredArgsConstructor
@RestController
@RequestMapping("/matching")
@CrossOrigin(origins = "https://silver-793a4.web.app", allowCredentials = "true")
public class MatchingController {

    @Autowired
    private final MatchingService matchingService;
    private final JwtService jwtService;

    //매칭 요청
    @PostMapping("/{receiver}")
    public BaseResponse matchingRequest(@PathVariable("receiver") Long receiver) {
        Long matchingId = matchingService.matchingRequest(receiver);
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


    //내가 받은 매칭 요청 조회(요청자의 프로필 전체 조회)
    @GetMapping("")
    public BaseResponse<List<ReceivedMatchingRes>> getReceivedMatchings() {
        try {
            Long userId = jwtService.getUserIdx();
            List<ReceivedMatchingRes> receivedMatchings = matchingService.getReceivedMatchings(userId);
            return new BaseResponse<>(receivedMatchings);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    //매칭된 친구 프로필 조회
    @GetMapping("/friend")
    public BaseResponse<List<GetUserRes>> getMyFriends () {
        try {
            Long userId = jwtService.getUserIdx();
            List<GetUserRes> friends = matchingService.getMatchedFriends(userId);
            return new BaseResponse<>(friends);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }
}
