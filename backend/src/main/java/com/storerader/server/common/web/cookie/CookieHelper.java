/*
 * File: common/web/cookie/CookieHelper.java
 * Description:
 *     웹 계층에서 사용하는 쿠키 관련 공통 유틸리티 클래스이다.
 *
 * Responsibilities:
 *      1) buildCookie()
 *          - 보안 정책이 적용된 쿠키 생성
 *
 *      2) deleteCookie()
 *         - 쿠키 삭제를 위한 만료 쿠키 생성
 *
 *      3) addCiookie()
 *         - 생성된 쿠키를 HTTP 응답 헤더에 추가
 *
 *      4) extractTokenFromCookie()
 *          - HTTP 요청 쿠키에서 특정 토큰 추출
 */

package com.storerader.server.common.web.cookie;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;

public class CookieHelper {
    // 쿠키 보안 설정
    @Value("${APP_COOKIE_SECURE}")
    private boolean cookieSecure;   // false
    @Value("${APP_COOKIE_SAME_SITE}")
    private String cookieSameSite;  // Lax

    /**
     * 보안 정책이 적용된 쿠키를 생성한다.
     *
     * @param name      쿠키의 이름
     * @param value     쿠키에 저장할 값
     * @param maxAgeSec 쿠키 유효 기간
     * @return 생성된 쿠키
     */
    public ResponseCookie buildCookie(
            String name,
            String value,
            long maxAgeSec
    ) {
        boolean secure = cookieSecure;
        String sameSite = cookieSameSite;

        if ("None".equalsIgnoreCase(sameSite)) {
            secure = true;
        }

        return ResponseCookie.from(name, value)
                .httpOnly(true)
                .secure(secure)
                .path("/")
                .maxAge(maxAgeSec)
                .sameSite(sameSite)
                .build();
    }

    /**
     * 쿠키를 삭제하기 위해 만료된 쿠키 객체를 생성한다.
     *
     * @param name 삭제할 쿠키의 이름
     * @return 만료된 쿠키
     */
    public ResponseCookie deleteCookie(
            String name
    ) {
        boolean secure = cookieSecure;
        String sameSite = cookieSameSite;

        if ("None".equalsIgnoreCase(sameSite)) {
            secure = true;
        }

        return ResponseCookie.from(name, "")
                .httpOnly(true)
                .secure(secure)
                .path("/")
                .maxAge(0)
                .sameSite(sameSite)
                .build();
    }

    /**
     * 최종 생성된 쿠키를 HTTP 응답 헤더에 추가한다.
     *
     * @param response 응답 객체
     * @param cookie   추가할 쿠키
     */
    public void addCookie(
            HttpServletResponse response,
            ResponseCookie cookie
    ) {
        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
    }

    /**
     * 요청 객체의 쿠키 목록에서 특정 이름을 가진 쿠키의 값을 찾는다
     *
     * @param request request HTTP 요청 객체
     * @param name    찾고자 하는 쿠키의 이름
     * @return 쿠키의 값
     */
    public String extractTokenFromCookie(
            HttpServletRequest request,
            String name
    ) {
        if (request.getCookies() == null) return null;

        for (Cookie cookie : request.getCookies()) {
            if (name.equals(cookie.getName())) {
                return cookie.getValue();
            }
        }
        return null;
    }
}
