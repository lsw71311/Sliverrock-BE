package com.example.silverrock.matching.Service;//package com.example.silverrock.matching.Service;

import com.example.silverrock.global.Response.BaseException;
import com.example.silverrock.login.jwt.JwtService;
import com.example.silverrock.matching.Entity.Matching;
import com.example.silverrock.matching.dto.PostMatcingReq;
import com.example.silverrock.matching.repository.MatchingRequestRepository;
import com.example.silverrock.user.User;
import com.example.silverrock.user.UserRepository;
import com.example.silverrock.user.dto.GetS3Res;
import com.example.silverrock.user.dto.GetUserRes;
import com.example.silverrock.user.profile.ProfileRepository;
import com.example.silverrock.user.profile.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.example.silverrock.global.Response.BaseResponseStatus.*;

import static com.example.silverrock.global.Response.BaseResponseStatus.MATCHING_NOT_FOUND;
import static com.example.silverrock.global.Response.BaseResponseStatus.USER_NOT_FOUND;

import static com.example.silverrock.global.Response.BaseResponseStatus.MATCHING_NOT_FOUND;
import static com.example.silverrock.global.Response.BaseResponseStatus.USER_NOT_FOUND;

@RequiredArgsConstructor
@Service
public class MatchingService {
    private final MatchingRequestRepository matchingRequestRepository;
    private final JwtService jwtService;
    private final ProfileService profileService;
    private final ProfileRepository profileRepository;
    private final UserRepository userRepository;

    public Long matchingRequest(PostMatcingReq postMatcingReq, Long receiverId) {
        Long senderId = jwtService.getUserIdx(); // 토큰에서 유저 고유번호 (sender) 받아오기
        User sender = userRepository.findUserById(senderId).orElse(null); // sender 정보(고유아이디, 폰넘버, 성별 등..) 가져오기
        User receiver = userRepository.findUserById(receiverId).orElse(null); // receiver 정보 가져오기

        if (sender == null || receiver == null) {// 유효한 유저 정보가 없는 경우 예외 처리
            throw new BaseException(USER_NOT_FOUND);
        }

        Matching matching = new Matching(
                sender, receiver, false // sender, receiver, 성공 여부 데이터 설정
        );
        matchingRequestRepository.save(matching); // 매칭 정보 저장
        return matching.getMatchingId(); // 생성된 매칭 아이디 반환
    }

    // 매칭 수락
    public void acceptMatching(Long matchingId) throws BaseException{
        Optional<Matching> existingMatching = matchingRequestRepository.findById(matchingId); //Optional객체: 해당 매칭 아이디가 있을 경우에만 처리

        if (existingMatching.isPresent()) {
            Matching matching = existingMatching.get(); //존재하는(입력받은) 매칭아이디에 해당하는 정보를 matching변수에 저장.
            matching.setSuccess(true); //matching변수에 속한 success정보를 true로 세팅
            matchingRequestRepository.save(matching); //변경 내용 저장

        } else {
            throw new BaseException(MATCHING_NOT_FOUND);
        }
    }

    // 매칭 거절
    public void rejectMatching(Long matchingId)throws BaseException {
        if (matchingRequestRepository.existsById(matchingId)) {
            matchingRequestRepository.deleteById(matchingId);
        } else {
            throw new BaseException(MATCHING_NOT_FOUND);
        }
    }

    //내가 받은 매칭 요청 조회
    public List<GetUserRes> getReceivedMatchings(Long userId) throws BaseException {
        User me = userRepository.findUserById(userId).orElse(null);   //id로 user객체 가져와
        List<Matching> receivedmatchings = matchingRequestRepository.findMatchingByReceiver(me).get();  // receiver가 '나'인 매칭 조회
        User sender;
        List<User> senders = new ArrayList<>();

        for(Matching matching : receivedmatchings){     //receiver가 '나'인 매칭 중
            if(matching.isSuccess() == false) {     //success가 false이면
                sender = matching.getSender();    //위에서 받은 매칭의 sender 받아와
                senders.add(sender);    //띄워줄 목록에 추가
            }
        }

        if(senders.isEmpty()){
            throw new BaseException(NONE_RECEIVED);
        }

        List<GetUserRes> receivedUserRes = senders.stream()
                .map(user -> new GetUserRes(user.getGender(), user.getNickname(), user.getBirth(), user.getRegion(), user.getIntroduce(),
                new GetS3Res(user.getProfile().getProfileUrl(), user.getProfile().getProfileFileName()))).collect(Collectors.toList());

        return receivedUserRes;    //sender 목록 반환
    }


    //매칭된 친구 조회
    public List<GetUserRes> getMatchedFriends(Long userId) throws BaseException {
        User me = userRepository.findUserById(userId).orElse(null);   //id로 user객체 가져와
        List<Matching> mymatchings = new ArrayList<>();     //receiver나 sender가 '나'인 매칭 담을
        List<Matching> receivedmatchings = matchingRequestRepository.findMatchingByReceiver(me).get();  // receiver가 '나'인 매칭 조회
        List<Matching> sentmatchings = matchingRequestRepository.findMatchingBySender(me).get();    //sender가 '나'인 매칭 조회
        User friend;
        List<User> friends = new ArrayList<>();

        for(Matching matching : receivedmatchings){     //receiver가 나 인 매칭들을 목록에 추가
            mymatchings.add(matching);
        }
        for(Matching matching : sentmatchings){         //sender가 나 인 매칭들을 목록에 추가
            mymatchings.add(matching);
        }

        for(Matching matching : mymatchings){
            if(matching.isSuccess() == true){   //매칭의 success가 true이면
                friend = matching.getSender();  //친구 목록에 추가
                friends.add(friend);
            }
        }

        if(friends.isEmpty()){
            throw new BaseException(NONE_FREIND);
        }

        List<GetUserRes> friendResList = friends.stream()
                .map(user -> new GetUserRes(user.getGender(), user.getNickname(), user.getBirth(), user.getRegion(), user.getIntroduce(),
                        new GetS3Res(user.getProfile().getProfileUrl(), user.getProfile().getProfileFileName()))).collect(Collectors.toList());

        return friendResList;    //친구 목록 반환
    }

}
