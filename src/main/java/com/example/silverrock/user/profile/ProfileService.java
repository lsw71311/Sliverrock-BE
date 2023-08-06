package com.example.silverrock.user.profile;

import com.example.silverrock.global.S3.S3Service;
import com.example.silverrock.user.User;
import com.example.silverrock.user.dto.GetS3Res;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProfileService {
    private final ProfileRepository profileRepository;
    private final S3Service s3Service;

    @Transactional
    public void saveProfile(GetS3Res getS3Res, User user){
        Profile profile;
        if(getS3Res.getImgUrl() != null) {
            profile = Profile.builder()
                    .profileUrl(getS3Res.getImgUrl())
                    .profileFileName(getS3Res.getFileName())
                    .user(user)
                    .build();
            profileRepository.save(profile);
        }
    }

    @Transactional
    public Profile findProfileById(Long memberId) {
        return profileRepository.findProfileById(memberId).orElse(null);
    }

    @Transactional
    public void deleteProfile(Profile profile) {
        s3Service.deleteFile(profile.getProfileFileName());
    }

    @Transactional
    public void deleteProfileById(Long memberId) {
        profileRepository.deleteProfileById(memberId);
    }
}
