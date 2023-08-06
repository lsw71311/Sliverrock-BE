package com.example.silverrock.login.jwt;

import com.example.silverrock.global.Response.BaseException;
import com.example.silverrock.global.UtilService;
import com.example.silverrock.user.User;
import com.example.silverrock.user.UserRepository;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.security.Key;

import static com.example.silverrock.global.Response.BaseResponseStatus.*;


@Service
@RequiredArgsConstructor
public class JwtService {
    private Key key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(Secret.JWT_SECRET_KEY));
    private final JwtProvider jwtProvider;
    private final UtilService utilService;
    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;

    /**
     * Header에서 Authorization 으로 JWT 추출
     */
    public String getJwt(){
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        return request.getHeader("Authorization");
    }

    public String getRefJwt(){
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        return request.getHeader("AuthorizationRef");
    }

    /**
     * JWT에서 userId 추출
     */
    public Long getUserIdx() throws BaseException {
        // 1. JWT 추출
        String accessToken = getJwt();
        if (accessToken == null || accessToken.length() == 0) {
            throw new BaseException(EMPTY_JWT);
        }
        try {
            // 2. JWT parsing
            Jws<Claims> claims = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(accessToken);
            // 3. userId 추출
            Long userId = claims.getBody().get("userId", Long.class);
            User user = utilService.findByUserIdWithValidation(userId);
            return userId;
        } catch (ExpiredJwtException e) {
            // access token이 만료된 경우
            User user = tokenRepository.findUserByAccessToken(accessToken).orElse(null);
            if (user == null) {
                throw new BaseException(INVALID_JWT);
            }
            // 4. Refresh Token을 사용하여 새로운 Access Token 발급
            Token token = tokenRepository.findTokenByUserId(user.getId()).orElse(null);
            String refreshToken = token.getRefreshToken();
            if (refreshToken != null) {
                String newAccessToken = refreshAccessToken(user, refreshToken);
                // 새로운 Access Token으로 업데이트된 JWT를 사용하여 userId 추출
                Jws<Claims> newClaims = Jwts.parserBuilder()
                        .setSigningKey(key)
                        .build()
                        .parseClaimsJws(newAccessToken);
                return newClaims.getBody().get("userId", Long.class);
            } else {
                throw new BaseException(EMPTY_JWT);
            }
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            throw new BaseException(INVALID_JWT);
        } catch (Exception ignored) {
            throw new BaseException(INVALID_JWT);
        }
    }

    /**
     * 로그아웃 전용 userId 추출 메서드
     */
    // 로그아웃을 시도할 때는 accsee token과 refresh 토큰이 만료되었어도
    // 형식만 유효하다면 토큰 재발급 없이 로그아웃 할 수 있어야 함.
    public Long getLogoutUserIdx() throws BaseException {

        // 1. JWT 추출
        String accessToken = getJwt();
        if (accessToken == null || accessToken.length() == 0) {
            throw new BaseException(EMPTY_JWT);
        }

        try {
            // 2. JWT parsing
            Jws<Claims> claims = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(accessToken);
            // 3. userId 추출
            return claims.getBody().get("userId", Long.class);
        } catch (ExpiredJwtException e) {
            // access token이 만료된 경우
            return 0L;
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            throw new BaseException(INVALID_JWT);
        } catch (Exception ignored) {
            throw new BaseException(INVALID_JWT);
        }
    }

    /**
     * 액세스 토큰 재발급
     */
    private String refreshAccessToken(User user, String refreshToken) throws BaseException {
        try {
            // 리프레시 토큰이 만료 등의 이유로 유효하지 않은 경우
            if (!jwtProvider.validateToken(refreshToken)) {
                throw new BaseException(INVALID_JWT);
            }
            else { // 리프레시 토큰이 유효한 경우
                Long userId = user.getId();
                String refreshedAccessToken = jwtProvider.createToken(userId);
                // 액세스 토큰 재발급에 성공한 경우
                if (refreshedAccessToken != null) {
                    Token token = utilService.findTokenByUserIdWithValidation(userId);
                    token.updateAccessToken(refreshedAccessToken);
                    tokenRepository.save(token);
                    return refreshedAccessToken;
                }
                throw new BaseException(FAILED_TO_REFRESH);
            }
        } catch (BaseException exception) {
            throw new BaseException(exception.getStatus());
        }
    }


    public Long getUserIdx(String accessToken) throws BaseException {
        // 1. JWT 추출
        if (accessToken == null || accessToken.length() == 0) {
            throw new BaseException(EMPTY_JWT);
        }
        try {
            // 2. JWT parsing
            Jws<Claims> claims = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(accessToken);
            // 3. userId 추출
            return claims.getBody().get("userId", Long.class);
        } catch (ExpiredJwtException e) {
            // access token이 만료된 경우
            User user = tokenRepository.findUserByAccessToken(accessToken).orElse(null);
            if(user == null) {
                throw new BaseException(INVALID_USER_JWT);
            }
            // 4. Refresh Token을 사용하여 새로운 Access Token 발급
            Token token = utilService.findTokenByUserIdWithValidation(user.getId());
            String refreshToken = token.getRefreshToken();
            if (refreshToken != null) {
                String newAccessToken = refreshAccessToken(user, refreshToken);
                // 새로운 Access Token으로 업데이트된 JWT를 사용하여 userId 추출
                Jws<Claims> newClaims = Jwts.parserBuilder()
                        .setSigningKey(key)
                        .build()
                        .parseClaimsJws(newAccessToken);
                return newClaims.getBody().get("memberId", Long.class);
            } else {
                throw new BaseException(EMPTY_JWT);
            }
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            throw new BaseException(INVALID_JWT);
        } catch (Exception ignored) {
            throw new BaseException(INVALID_JWT);
        }
    }
}
