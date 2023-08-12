package com.example.silverrock.matching.Service;

import com.example.silverrock.global.Response.BaseException;
import com.example.silverrock.login.jwt.JwtService;
import com.example.silverrock.matching.Entity.Matching;
import com.example.silverrock.matching.dto.PostMatcingReq;
import com.example.silverrock.matching.repository.MatchingRequestRepository;
import com.example.silverrock.user.User;
import com.example.silverrock.user.UserRepository;
import com.example.silverrock.user.dto.PostLoginRes;
import com.example.silverrock.user.profile.Profile;
import com.example.silverrock.user.profile.ProfileRepository;
import com.example.silverrock.user.profile.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

    //매칭 요청 matchingRequest
//    public Long matchingRequest(PostMatcingReq postMatcingReq){
//        Long userId = jwtService.getUserIdx(); //토큰에서 유저고유번호 받아오기...!
//        User user = userRepository.findUserById(userId).orElse(null);   //id로 user객체 가져와
//        Matching matching =new Matching(
//                postMatcingReq.getSender(), postMatcingReq.getReceiver(), false
//                //요청 수신자와 발신자 고유번호를 받아옴. 성공여부는 false기본값으로 저장
//        );
//        matchingRequestRepository.save(matching);  //매칭 정보들 저장. 이거 스프링 프레임워크에서 자동으로 만들어줘야 하는건가?!
//
//        return matching.getMatchingId(); //디비에 자동 생성 저장된 매칭아이디 가져와서 프론트에 반환해주기
//    }
    public Long matchingRequest(Long receiverId) {
        Long senderId = jwtService.getUserIdx(); // 토큰에서 유저 고유번호 (sender) 받아오기
        User sender = userRepository.findUserById(senderId).orElse(null); // sender 정보(고유아이디, 폰넘버, 성별 등..) 가져오기
        User receiver = userRepository.findUserById(receiverId).orElse(null); // receiver 정보 가져오기

        if (sender == null || receiver == null) {// 유효한 유저 정보가 없는 경우 예외 처리
            throw new BaseException(USER_NOT_FOUND);
        }

        Matching matching = new Matching(
                sender.getId(), receiver.getId(), false // sender, receiver, 성공 여부 데이터 설정
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
    public List<Profile> getReceivedMatchingProfiles() throws BaseException {
        Long userId = jwtService.getUserIdx();      //나의 id 가져와
        User user = userRepository.findUserById(userId).orElse(null);   //id로 user객체 가져와
        List<Matching> matchings = matchingRequestRepository.findMatchingByReceiver(user).get();  // receiver가 '나'인 매칭 조회
//        Long senderId;
        Long sender;
        List<Profile> receivedProfiles = new ArrayList<>();

        for(Matching matching : matchings){
            sender = matching.getSender();    //위에서 받은 매칭의 sender 받아와
//            Profile profile = profileRepository.findProfileById(senderId).orElse(null);     //sender id로 해당 프로필 조회
            Profile profile = profileRepository.findProfileById(sender).orElse(null);
            receivedProfiles.add(profile);      //해당 프로필 목록에 추가
        }

        return receivedProfiles;    //sender 프로필 목록 반환
    }

    //매칭된 친구 프로필 조회
    public List<Profile> getMatchedFriends() throws BaseException{
        Long userId = jwtService.getUserIdx();      //나의 id 가져와
        User user = userRepository.findUserById(userId).orElse(null);   //id로 user객체 가져와
        List<Matching> matchings = matchingRequestRepository.findMatchingByReceiver(user).get();  //receiver가 '나'인 매칭 조회
//        Long senderId;
        Long sender;
        List<Profile> friends = new ArrayList<>();

        for(Matching matching : matchings){
            if(matching.isSuccess() == true){   //매칭의 success가 true인 경우(매칭된 경우)
                sender = matching.getSender();    //해당 매칭의 sender 받아와서
//                Profile profile = profileRepository.findProfileById(senderId).orElse(null);
                Profile profile = profileRepository.findProfileById(sender).orElse(null);
                friends.add(profile);   //매칭된 친구 목록에 프로필 추가
            }
        }

        return friends;     //친구 목록 반환
    }

}

