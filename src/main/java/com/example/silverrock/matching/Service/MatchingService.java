package com.example.silverrock.matching.Service;//package com.example.silverrock.matching.Service;

import com.example.silverrock.global.Response.BaseException;
import com.example.silverrock.login.jwt.JwtService;
import com.example.silverrock.matching.Entity.Matching;
import com.example.silverrock.matching.dto.PostMatcingReq;
import com.example.silverrock.matching.repository.MatchingRequestRepository;
import com.example.silverrock.user.User;
import com.example.silverrock.user.UserRepository;
import com.example.silverrock.user.profile.Profile;
import com.example.silverrock.user.profile.ProfileRepository;
import com.example.silverrock.user.profile.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class MatchingService {
    private final MatchingRequestRepository matchingRequestRepository;
    private final JwtService jwtService;
    private final ProfileService profileService;
    private final ProfileRepository profileRepository;
    private final UserRepository userRepository;

//    //매칭 요청 MatchingRequest
//    public MatchingRequest(){
//        //매칭고유번호 생성, 매칭여부=false
//
//
//    }
//
//    //매칭 수락
//    public void match(PostMatcingReq postMatcingReq){
//        Matching matching =new Matching(
//                postMatcingReq.getSender(), postMatcingReq.getReceiver(), false
//        );
//        matchingR.save(matching);
//    }
//
//    //매칭 거절



    //내가 받은 매칭 요청 조회
    public List<Profile> getReceivedMatchingProfiles() throws BaseException {
        Long userId = jwtService.getUserIdx();      //나의 id 가져와
        User user = userRepository.findUserById(userId).orElse(null);   //id로 user객체 가져와
        List<Matching> matchings = matchingRequestRepository.findMatchingByReceiver(user).get();  // receiver가 '나'인 매칭 조회
//        Long senderId;
        User sender;
        List<Profile> receivedProfiles = new ArrayList<>();

        for(Matching matching : matchings){
            sender = matching.getSender();    //위에서 받은 매칭의 sender 받아와
//            Profile profile = profileRepository.findProfileById(senderId).orElse(null);     //sender id로 해당 프로필 조회
            Profile profile = profileRepository.findProfileByUser(sender).orElse(null);
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
        User sender;
        List<Profile> friends = new ArrayList<>();

        for(Matching matching : matchings){
            if(matching.isSuccess() == true){   //매칭의 success가 true인 경우(매칭된 경우)
                sender = matching.getSender();    //해당 매칭의 sender 받아와서
//                Profile profile = profileRepository.findProfileById(senderId).orElse(null);
                Profile profile = profileRepository.findProfileByUser(sender).orElse(null);
                friends.add(profile);   //매칭된 친구 목록에 프로필 추가
            }
        }

        return friends;     //친구 목록 반환
    }

}
