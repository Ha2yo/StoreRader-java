package com.storerader.server.domain.auth.controller;

import com.storerader.server.common.exception.CustomException;
import com.storerader.server.common.exception.ExceptionClass;
import com.storerader.server.domain.auth.dto.response.GoogleLoginRes;
import com.storerader.server.domain.auth.model.GoogleLoginResult;
import com.storerader.server.domain.auth.dto.request.GoogleLoginReq;
import com.storerader.server.domain.auth.dto.request.RefreshAccessTokenReq;
import com.storerader.server.domain.auth.dto.response.RefreshAccessTokenRes;
import com.storerader.server.domain.auth.service.AuthService;
import com.storerader.server.common.web.cookie.CookieHelper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "인증 API", description = "인증 및 세션 관리 API")
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final CookieHelper cookieHelper;

    private static final long ACCESS_TOKEN_EXPIRATION =
            30 * 60;            // 30분
    private static final long REFRESH_TOKEN_EXPIRATION =
            7 * 24 * 60 * 60;   // 7일

    /**
     * 구글 ID Token으로 로그인을 시도한다.
     * 성공 시 Access/Refresh Token을 응답 헤더의 쿠키에 설정한다.
     *
     * @param request  Google ID Token
     * @param response 헤더를 추가하기 위한 응답 객체
     * @return 로그인 성공 유저 정보 및 토큰 정보
     */
    @Operation(
            summary = "구글 로그인 수행",
            description = "Google ID Token으로 로그인 처리 후 Access/Refresh Token을 쿠키로 설정합니다."
    )
    @PostMapping("/google")
    public ResponseEntity<GoogleLoginRes> googleLogin(
            @RequestBody GoogleLoginReq request,
            HttpServletResponse response
    ) {
        // Google ID Token 검증 및 유저 로그인 처리
        GoogleLoginResult loginResponse = authService.googleLogin(request);

        // Refresh/Access Token 쿠키 설정
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

        // 로그인된 유저 정보 추출 및 반환
        GoogleLoginResult.AuthUser user = loginResponse.user();

        GoogleLoginRes res = new GoogleLoginRes(
                new GoogleLoginRes.UserResponse(
                        user.name(),
                        user.email(),
                        user.picture(),
                        user.role()
                )
        );

        return ResponseEntity.ok(res);
    }

    /**
     * Refresh Token을 사용해 AccessToken을 재발급한다.
     *
     * @param refreshAccessTokenReq refreshToken을 포함한 요청 바디
     * @param request               쿠키에서 refreshToken을 추출하기 위한 요청 객체
     * @param response              새 accessToken 쿠키를 설정하기 위한 응답 객체
     * @return 새로 발급된 accessToken
     */
    @Operation(
            summary = "Access Token 재발급",
            description = "Refresh 토큰으로 새로운 Access Token을 발급하고, accessToken 쿠키를 갱신합니다."
    )
    @PostMapping("/refresh")
    public ResponseEntity<RefreshAccessTokenRes> refreshAccessToken(
            @RequestBody(required = false) RefreshAccessTokenReq refreshAccessTokenReq,
            HttpServletRequest request,
            HttpServletResponse response
    ) throws CustomException {
        String refreshToken = null;

        // refreshToken 추출
        if (refreshAccessTokenReq != null) {
            refreshToken = refreshAccessTokenReq.refreshToken();
        }

        if (refreshToken == null || refreshToken.isBlank()) {
            refreshToken = cookieHelper.extractTokenFromCookie(request, "refreshToken");
        }

        if (refreshToken == null || refreshToken.isBlank()) {
            throw new CustomException(
                    ExceptionClass.REFRESH_TOKEN_NOT_FOUND,
                    ExceptionClass.REFRESH_TOKEN_NOT_FOUND.getStatus(),
                    ExceptionClass.REFRESH_TOKEN_NOT_FOUND.getMessage()
            );
        }

        // Access Token 재발급
        String newAccessToken = authService.refreshAccessToken(refreshToken);

        // 새 Access Token 쿠키 설정
        cookieHelper.addCookie(
                response,
                cookieHelper.buildCookie(
                        "accessToken",
                        newAccessToken,
                        ACCESS_TOKEN_EXPIRATION
                )
        );

        return ResponseEntity.ok(new RefreshAccessTokenRes(newAccessToken));
    }

    /**
     * 현재 로그인한 유저의 정보를 조회한다.
     *
     * @param request 현재 요청 객체
     * @return 로그인한 유저의 정보 (id, name, email..)
     */
    @Operation(
            summary = "내 정보 조회",
            description = "쿠키를 기반으로 현재 로그인한 유저의 정보를 조회합니다."
    )
    @GetMapping("/me")
    public ResponseEntity<?> getMyInfo(
            HttpServletRequest request
    ) throws CustomException {
        String accessToken = cookieHelper.extractTokenFromCookie(request, "accessToken");

        if (accessToken == null || accessToken.isBlank()) {
            throw new CustomException(
                    ExceptionClass.UNAUTHORIZED,
                    ExceptionClass.UNAUTHORIZED.getStatus(),
                    ExceptionClass.UNAUTHORIZED.getMessage()
            );
        }

        try {
            return ResponseEntity.ok(authService.getMyInfo(accessToken));
        } catch (RuntimeException e) {
            throw new CustomException(
                    ExceptionClass.TOKEN_INVALID,
                    ExceptionClass.TOKEN_INVALID.getStatus(),
                    e.getMessage()
            );
        }
    }

    /**
     * 로그아웃을 처리한다.
     *
     * @param response 쿠키 삭제 헤더를 추가하기 위한 응답 객체
     * @return 성공 시 OK
     */
    @Operation(
            summary = "유저 로그아웃",
            description = "accessToken/refreshToken 쿠키를 삭제합니다."
    )
    @PostMapping("/logout")
    public ResponseEntity<?> logout(
            HttpServletResponse response
    ) {
        cookieHelper.addCookie(response, cookieHelper.deleteCookie("accessToken"));
        cookieHelper.addCookie(response, cookieHelper.deleteCookie("refreshToken"));
        return ResponseEntity.ok().build();
    }
}