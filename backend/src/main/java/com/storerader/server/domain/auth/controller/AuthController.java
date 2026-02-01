/*
 * File: domain/auth/controller/AuthController.java
 * Description:
 *     인증(auth) 도메인의 컨트롤러 계층으로,
 *     클라이언트의 인증/인가 관련 요청을 받아 서비스 계층으로 위임하고
 *     그 결과를 HTTP 응답 형태로 반환하다.
 *
 * Responsibilities:
 *      1) googleLogin()
 *         - Google ID Token으로 로그인 처리 후 토큰 쿠키 설정
 *
 *      2) refresh()
 *         - Refresh Token으로 새로운 Access Token 발급 및 쿠키 갱신
 *
 *      3) getMyInfo()
 *         - 현재 로그인한 유저 정보 조회
 *
 *      4) Logout()
 *          - 로그아웃 처리 (토큰 쿠키 삭제)
 */

package com.storerader.server.domain.auth.controller;

import com.storerader.server.domain.auth.dto.GoogleLoginRequestDTO;
import com.storerader.server.domain.auth.dto.GoogleLoginResponseDTO;
import com.storerader.server.domain.auth.dto.TokenRefreshRequestDTO;
import com.storerader.server.domain.auth.dto.TokenRefreshResponseDTO;
import com.storerader.server.domain.auth.service.AuthService;
import com.storerader.server.common.web.cookie.CookieHelper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    private final CookieHelper cookieHelper;

    private static final long ACCESS_TOKEN_EXPIRATION = 30 * 60;           // 30분
    //    private static final long REFRESH_TOKEN_EXPIRATION = 7 * 24 * 60 * 60; // 7일
    private static final long REFRESH_TOKEN_EXPIRATION = 24 * 60 * 60; // 24시간

    /**
     * 구글 ID Token으로 로그인을 시도한다.
     * 성공 시 Access/Refresh Token을 응답 헤더의 쿠키에 설정한다.
     *
     * @param request  Google ID Token
     * @param response 헤더를 추가하기 위한 응답 객체
     * @return 로그인 성공 유저 정보 및 토큰 정보
     */
    @PostMapping("/google")
    public ResponseEntity<GoogleLoginResponseDTO> googleLogin(
            @RequestBody GoogleLoginRequestDTO request,
            HttpServletResponse response
    ) {
        GoogleLoginResponseDTO loginResponse = authService.googleLogin(request);

        cookieHelper.addCookie(
                response, cookieHelper.buildCookie(
                        "refreshToken",
                        loginResponse.refreshToken(),
                        REFRESH_TOKEN_EXPIRATION
                )
        );
        cookieHelper.addCookie(
                response, cookieHelper.buildCookie(
                        "accessToken",
                        loginResponse.accessToken(),
                        ACCESS_TOKEN_EXPIRATION
                )
        );

        return ResponseEntity.ok(loginResponse);
    }

    /**
     * Refresh Token을 사용하여 새로운 Access Token을 발급받는다.
     *
     * @param tokenRefreshRequest 쿠키에서 추출한 리프레시 토큰
     * @param response            새 액세스 토큰 쿠키 설정을 위한 응답 객체
     * @return 새로 발급된 액세스 토큰
     */
    @PostMapping("/refresh")
    public ResponseEntity<TokenRefreshResponseDTO> refresh(
            @RequestBody(required = false) TokenRefreshRequestDTO tokenRefreshRequest,
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        String refreshToken = null;

        if (tokenRefreshRequest != null) {
            refreshToken = tokenRefreshRequest.refreshToken();
        }

        if (refreshToken == null || refreshToken.isBlank()) {
            refreshToken = cookieHelper.extractTokenFromCookie(request, "refreshToken");
        }

        if (refreshToken == null || refreshToken.isBlank()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String newAccessToken = authService.refreshAccessToken(refreshToken);

        cookieHelper.addCookie(
                response,
                cookieHelper.buildCookie(
                        "accessToken",
                        newAccessToken,
                        ACCESS_TOKEN_EXPIRATION
                )
        );

        return ResponseEntity.ok(new TokenRefreshResponseDTO(newAccessToken));
    }

    /**
     * 현재 로그인한 유저의 정보를 조회한다.
     *
     * @param request 현재 요청 객체
     * @return 로그인한 유저의 정보 (id, name, email..)
     */
    @GetMapping("/me")
    public ResponseEntity<?> getMyInfo(
            HttpServletRequest request
    ) {
        String accessToken = cookieHelper.extractTokenFromCookie(request, "accessToken");

        if (accessToken == null || accessToken.isBlank()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("인증 토큰이 없습니다.");
        }

        try {
            return ResponseEntity.ok(authService.getMyInfo(accessToken));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }

    /**
     * 로그아웃을 처리한다.
     *
     * @param response 쿠키 삭제 헤더를 추가하기 위한 응답 객체
     * @return 성공 시 OK
     */
    @PostMapping("/logout")
    public ResponseEntity<?> logout(
            HttpServletResponse response
    ) {
        cookieHelper.addCookie(response, cookieHelper.deleteCookie("accessToken"));
        cookieHelper.addCookie(response, cookieHelper.deleteCookie("refreshToken"));
        return ResponseEntity.ok().build();
    }
}
