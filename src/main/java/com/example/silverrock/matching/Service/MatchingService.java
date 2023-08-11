package com.example.silverrock.matching.Service;

import com.example.silverrock.matching.Entity.Matching;
import com.example.silverrock.matching.dto.PostMatcingReq;
import com.example.silverrock.matching.repository.MatchingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class MatchingService {

    private final MatchingRepository matchingRepository;

    //매칭 요청 matchingRequest
    public void matchingRequest(PostMatcingReq postMatcingReq, Long matchingId){
        Matching matching =new Matching(
                postMatcingReq.getSender(), postMatcingReq.getReceiver(), false
                //요청 수신자와 발신자 고유번호를 받아옴. 성공여부는 false기본값으로 저장
        );
        matchingRepository.save(matching);  //매칭 정보들 저장. 이거 스프링 프레임워크에서 자동으로 만들어줘야 하는건가?!
    }

    // 매칭 수락
    public void acceptMatching(Long matchingId) {
        Optional<Matching> existingMatching = matchingRepository.findById(matchingId);

        existingMatching.ifPresent(matching -> { //Optional객체: 해당 매칭 아이디가 있을 경우에만 처리
            matching.setSuccess(true);
            matchingRepository.save(matching);
        });
    }

    // 매칭 거절
    public void rejectMatching(Long matchingId) {
        matchingRepository.deleteById(matchingId);
    }
}
