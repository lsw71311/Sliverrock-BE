package com.example.silverrock.user;

import com.example.silverrock.global.Response.BaseException;
import com.example.silverrock.global.Response.BaseResponse;
import com.example.silverrock.login.jwt.JwtService;
import com.example.silverrock.user.dto.*;
import com.example.silverrock.user.dto.PostLoginReq;
import com.example.silverrock.user.dto.PostLoginRes;
import com.example.silverrock.user.dto.PostUserReq;
import com.example.silverrock.user.dto.PostUserRes;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;
    private final JwtService jwtService;


    /**
     * 닉네임 중복확인
     */
    @GetMapping("/nickname")
    public BaseResponse<String> checkNickname(@RequestParam(name="nickname") String nickname){
        try {
            return new BaseResponse<>(userService.checkNickname(nickname));
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }


    /**
     * 회원 가입
     */
    @PostMapping("")
    public BaseResponse<PostUserRes> createUser(@RequestPart(value = "profile", required = false) MultipartFile multipartFile,
                                                @Validated @RequestPart(value = "postUserReq") PostUserReq postUserReq){
        try {
            return new BaseResponse<>(userService.createUser(postUserReq, multipartFile));
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }


    /**
     * 로그인
    */
    @PostMapping("/login")
    public BaseResponse<PostLoginRes> loginMember(@RequestBody PostLoginReq postLoginReq){
        try{
            return new BaseResponse<>(userService.login(postLoginReq));
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * 로그아웃
    */
    @PostMapping("/logout")
    public BaseResponse<String> logoutUser() {
        try {
            Long userId = jwtService.getLogoutUserIdx(); // 토큰 만료 상황에서 로그아웃을 시도하면 0L을 반환
            return new BaseResponse<>(userService.logout(userId));
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }


    /**
     * 유저 프로필 사진 변경
     */
    @PatchMapping("/profile")
    public BaseResponse<String> modifyProfile(@RequestPart(value = "image", required = false) MultipartFile multipartFile) {
        try {
            Long userId = jwtService.getUserIdx();
            return new BaseResponse<>(userService.modifyProfile(userId, multipartFile));
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }



    /**
     * 근처 친구 전체 조회 (메인화면)
     */
    @GetMapping("/near")
    public BaseResponse<List<GetNearUserRes>> getNearUser(){
        try {
            Long userId = jwtService.getUserIdx();
            return new BaseResponse<>(userService.getProfilesByRegion(userId));
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    //내 정보 조회
    @GetMapping("/myinfo")
    public BaseResponse<GetUserInfoRes> getMyInfo(){
        try{
            Long userId = jwtService.getUserIdx();
            return new BaseResponse<>(userService.getUserInfo(userId));
        }catch (BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
    }

    //내 정보 수정(프로필 사진 제외)
    @PatchMapping("/myinfo/modify")
    public BaseResponse<GetUserInfoRes> modifyMyInfo(@RequestBody GetUserInfoReq userInfoReq){
        try{
            Long userId = jwtService.getUserIdx();
            return new BaseResponse<>(userService.modifyUserInfo(userId, userInfoReq));
        }catch (BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
    }

}

