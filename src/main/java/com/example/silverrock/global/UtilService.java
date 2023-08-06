package com.example.silverrock.global;


import com.example.silverrock.global.Response.BaseException;
import com.example.silverrock.login.jwt.Token;
import com.example.silverrock.login.jwt.TokenRepository;
import com.example.silverrock.user.User;
import com.example.silverrock.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

import static com.example.silverrock.global.Response.BaseResponseStatus.INVALID_USER_JWT;

@Service
@RequiredArgsConstructor
public class UtilService {
    public static final int SEC = 60;
    public static final int MIN = 60;
    public static final int HOUR = 24;
    public static final int DAY = 30;
    public static final int MONTH = 12;


    private final TokenRepository tokenRepository;
    private final UserRepository userRepository;

    public User findByUserIdWithValidation(Long userId) throws BaseException {
        return userRepository.findUserById(userId)
                .orElseThrow(() -> new BaseException(INVALID_USER_JWT));
    }


    public Token findTokenByUserIdWithValidation(Long userId) throws BaseException {
        return tokenRepository.findTokenByUserId(userId)
                .orElseThrow(() -> new BaseException(INVALID_USER_JWT));
    }



    public static String convertLocalDateTimeToLocalDate(LocalDateTime localDateTime) {
        return localDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }
    public static String convertLocalDateTimeToTime(LocalDateTime localDateTime) {
        LocalDateTime now = LocalDateTime.now();

        long diffTime = localDateTime.until(now, ChronoUnit.SECONDS); // now보다 이후면 +, 전이면 -

        if (diffTime < SEC){
            return diffTime + "초 전";
        }
        diffTime = diffTime / SEC;
        if (diffTime < MIN) {
            return diffTime + "분 전";
        }
        diffTime = diffTime / MIN;
        if (diffTime < HOUR) {
            return diffTime + "시간 전";
        }
        diffTime = diffTime / HOUR;
        if (diffTime < DAY) {
            return diffTime + "일 전";
        }
        diffTime = diffTime / DAY;
        if (diffTime < MONTH) {
            return diffTime + "개월 전";
        }
        diffTime = diffTime / MONTH;
        return diffTime + "년 전";
    }

    public static String formatTime(LocalTime time) {
        int hour = time.getHour();
        int min = time.getMinute();
        String meridiem = (hour >= 12) ? "오후" : "오전";
        if (hour >= 12) {
            hour -= 12;
        }
        return meridiem + " " + hour + ":" + String.format("%02d", min);
    }
}
