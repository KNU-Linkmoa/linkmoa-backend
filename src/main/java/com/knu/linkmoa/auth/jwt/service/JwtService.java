package com.knu.linkmoa.auth.jwt.service;

import com.knu.linkmoa.auth.jwt.dto.response.TokenStatus;
import com.knu.linkmoa.auth.jwt.error.JwtErrorCode;
import com.knu.linkmoa.domain.member.entity.Member;
import com.knu.linkmoa.domain.member.repository.MemberRepository;
import com.knu.linkmoa.global.error.custom.CookieNotFoundException;
import com.knu.linkmoa.global.error.custom.NotValidTokenException;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.Cookie;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Service
@Slf4j
@RequiredArgsConstructor
public class JwtService {
    private SecretKey secretKey;

    @Value("${spring.jwt.secret}")
    private String secret;
    private final Long ACCESS_TOKEN_EXPIRATION = 1000L * 60 * 60;
    private final Long REFRESH_TOKEN_EXPIRATION = 1000 * 60 * 60 * 24 * 14L;


    private final MemberRepository memberRepository;

    @PostConstruct
    private void init() {
        this.secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8),
                Jwts.SIG.HS256.key().build().getAlgorithm());
    }

    public String getUsername(String token) {
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("username", String.class);
    }

    public String getEmail(String token) {

        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("email", String.class);
    }

    public String getRole(String token) {
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("role", String.class);
    }

    public Boolean isExpired(String token) {

        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().getExpiration().before(new Date());
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token);
            return true;
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            log.info("Invalid JWT Token", e);
            throw new NotValidTokenException(JwtErrorCode.NOT_VALID_TOKEN_EXCEPTION, TokenStatus.INVALID);
        } catch (ExpiredJwtException e) {
            log.info("Expired JWT Token", e);
            if (isRefreshTokenExpired(e)) {
                throw new NotValidTokenException(JwtErrorCode.EXPIRED_REFRESH_TOKEN_EXCEPTION, TokenStatus.REFRESH_TOKEN_EXPIRED);
            }
            throw new NotValidTokenException(JwtErrorCode.EXPIRED_ACCESS_TOKEN_EXCEPTION, TokenStatus.ACCESS_TOKEN_EXPIRED);
        } catch (UnsupportedJwtException e) {
            log.info("Unsupported JWT Token", e);
            throw new NotValidTokenException(JwtErrorCode.UNSUPPORTED_TOKEN_EXCEPTION, TokenStatus.UNSUPPORTED);
        } catch (IllegalArgumentException e) {
            log.info("JWT claims string is empty.", e);
            throw new NotValidTokenException(JwtErrorCode.MISMATCH_CLAIMS_EXCEPTION, TokenStatus.MISMATCH_CLAIMS);
        }
    }

    public String createAccessToken(String email, String role) {
        return Jwts.builder()
                .claim("email", email)
                .claim("role", role)
                .claim("token_type", "access_token")
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + ACCESS_TOKEN_EXPIRATION))
                .signWith(secretKey)
                .compact();
    }

    public String createRefreshToken() {
        return Jwts.builder()
                .claim("token_type", "refresh_token")
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + REFRESH_TOKEN_EXPIRATION))
                .signWith(secretKey)
                .compact();
    }

    /**
     * 나중에 Redis로 바꾸자
     */
    public void saveRefreshToken(String email, String refresh) {

        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("member가 없어요"));

        member.updateRefreshToken(refresh);
        memberRepository.save(member);
    }

    public Cookie createCookie(String key, String value) {
        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(14 * 24 * 60 * 60);
        cookie.setPath("/");
        cookie.setHttpOnly(true);

        return cookie;
    }

    public String getRefreshTokenFromCookies(Cookie[] cookies){
        String refresh = null;

        if(cookies == null){
            throw new CookieNotFoundException("Cookie가 하나도 없습니다. Cookie를 담아서 요청해주세요");
        }

        for (Cookie cookie : cookies) {
            if(cookie.getName().equals("Authorization-refresh")){
                refresh = cookie.getValue();
            }
        }

        if(refresh == null){
            throw new CookieNotFoundException("Authorization-refresh에 해당하는 쿠키를 찾지 못했습니다. Cookie를 확인해주세요");
        }

        return refresh;
    }

    public Member findMemberByRefresh(String refreshToken){
        return memberRepository.findByRefresh(refreshToken)
                .orElseThrow(() -> new NotValidTokenException(JwtErrorCode.REFRESH_TOKEN_MISMATCH_EXCEPTION, TokenStatus.REFRESH_TOKEN_MISMATCH));
    }

    private static boolean isRefreshTokenExpired(ExpiredJwtException e) {
        return e.getClaims().get("token_type").equals("refresh_token");
    }
}