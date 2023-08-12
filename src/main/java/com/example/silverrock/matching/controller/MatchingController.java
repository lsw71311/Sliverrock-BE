package com.example.silverrock.matching.controller;

import com.example.silverrock.global.Response.BaseException;
import com.example.silverrock.global.Response.BaseResponse;
import com.example.silverrock.login.jwt.JwtService;
import com.example.silverrock.global.Response.BaseResponseStatus;
import com.example.silverrock.matching.Service.MatchingService;
import com.example.silverrock.matching.dto.PostMatcingReq;
import com.example.silverrock.user.dto.GetUserRes;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/matching")
public class MatchingController {

    @Autowired
    private final MatchingService matchingService;
    private final JwtService jwtService;

    //매칭 요청
    @PostMapping("/{receiver}")
    public BaseResponse matchingRequest(@PathVariable("receiver") Long receiver, @RequestBody PostMatcingReq postMatcingReq) {
        Long matchingId = matchingService.matchingRequest(postMatcingReq,receiver);
        return new BaseResponse<>(matchingId);
    }

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
    public BaseResponse<List<GetUserRes>> getReceivedMatchings() {
        try {
            Long userId = jwtService.getUserIdx();
            List<GetUserRes> receivedMatchings = matchingService.getReceivedMatchings(userId);
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
