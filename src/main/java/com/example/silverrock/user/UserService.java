package com.example.silverrock.user;


import com.example.silverrock.global.Response.BaseException;
import com.example.silverrock.global.S3.S3Service;
import com.example.silverrock.global.UtilService;
import com.example.silverrock.login.dto.JwtResponseDTO;
import com.example.silverrock.login.jwt.*;
import com.example.silverrock.user.dto.*;
import com.example.silverrock.user.profile.ProfileRepository;
import com.example.silverrock.user.profile.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import com.example.silverrock.global.AES128;

import static com.example.silverrock.global.Response.BaseResponseStatus.*;


@EnableTransactionManagement
@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final UtilService utilService;
    private final JwtProvider jwtProvider;
    private final JwtService jwtService;
    private final ProfileService profileService;
    private final ProfileRepository profileRepository;
    private final S3Service s3Service;

    /**
     * 유저 생성 후 DB에 저장(회원 가입) with JWT
     */
    @Transactional
    public PostUserRes createUser(PostUserReq postUserReq, MultipartFile multipartFile) throws BaseException {
        if(postUserReq.getPassword().isEmpty()){
            throw new BaseException(PASSWORD_CANNOT_BE_NULL);
        }
        if(!postUserReq.getPassword().equals(postUserReq.getPasswordChk())) {
            throw new BaseException(PASSWORD_MISSMATCH);
        }
        if(userRepository.existsByNickname(postUserReq.getNickname())) {
            throw new BaseException(DUPLICATED_NICKNAME);
        }
        String pwd;
        try{
            pwd = new AES128(Secret.USER_INFO_PASSWORD_KEY).encrypt(postUserReq.getPassword()); // 암호화 코드
        }
        catch (Exception ignored) { // 암호화가 실패하였을 경우 에러 발생
            throw new BaseException(PASSWORD_ENCRYPTION_ERROR);
        }
        User user = new User(postUserReq.getPhoneNum(), postUserReq.getGender(), postUserReq.getNickname(),
                postUserReq.getBirth(), postUserReq.getRegion(), pwd, postUserReq.getIntroduce());
        userRepository.save(user);
        GetS3Res getS3Res;
        if(multipartFile != null) {
            getS3Res = s3Service.uploadSingleFile(multipartFile);
            profileService.saveProfile(getS3Res, user);
        }

        return new PostUserRes(user);
    }

    /**
     * 유저 로그인 with JWT
    */
    public PostLoginRes login(PostLoginReq postLoginReq) throws BaseException {
        User user = userRepository.findUserByPhoneNum(postLoginReq.getPhoneNum()).get();
        Token existToken = tokenRepository.findTokenByUserId(user.getId()).orElse(null);
        if (existToken != null) {
            throw new BaseException(ALREADY_LOGIN);
        }

        String password;
        try {
            password = new AES128(Secret.USER_INFO_PASSWORD_KEY).decrypt(user.getPassword());
        } catch (Exception ignored) {
            throw new BaseException(PASSWORD_DECRYPTION_ERROR);
        }

        if (postLoginReq.getPassword().equals(password)) {
            JwtResponseDTO.TokenInfo tokenInfo = jwtProvider.generateToken(user.getId());
            Token token = Token.builder()
                    .accessToken(tokenInfo.getAccessToken())
                    .refreshToken(tokenInfo.getRefreshToken())
                    .user(user)
                    .build();
            tokenRepository.save(token);
            return new PostLoginRes(user, token);
        } else {
            throw new BaseException(FAILED_TO_LOGIN);
        }
    }

    /**
     * 유저 로그아웃
    */
    @Transactional
    public String logout(Long userId) throws BaseException {
        try {
            if (userId == 0L) { // 로그아웃 요청은 access token이 만료되더라도 재발급할 필요가 없음.
                User user = tokenRepository.findUserByAccessToken(jwtService.getJwt()).orElse(null);
                if (user != null) {
                    Token token = tokenRepository.findTokenByUserId(user.getId()).orElse(null);
                    tokenRepository.deleteTokenByAccessToken(token.getAccessToken());
                    return "로그아웃 되었습니다.";
                }
                else {
                    throw new BaseException(INVALID_JWT);
                }
            }
            else { // 토큰이 만료되지 않은 경우
                User logoutUser = utilService.findByUserIdWithValidation(userId);
                //리프레쉬 토큰 삭제
                tokenRepository.deleteTokenByUserId(logoutUser.getId());
                return "로그아웃 되었습니다.";
            }
        } catch (Exception e) {
            throw new BaseException(FAILED_TO_LOGOUT);
        }

    }



}
