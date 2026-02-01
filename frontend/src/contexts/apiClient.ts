/**
 * File: contexts/apiClient.tsx
 * Description:
 *   StoreRader 웹 에플리케이션에서 사용하는 공통 Axios 클라이언트
 *
 * Responsibilities:
 *   1. 실행 환경(개발/운영)에 따라 baseURL을 결정
 *   2. 쿠키 기반 인증을 위해 withCredentials 적용
 *   3. 401(Access Token 만료) 발생 시 /auth/refresh 호출 후 원 요청 1회 재시도
 */

import axios from "axios";

const getBaseURL = () => {
  const { hostname } = window.location;

  if (hostname === "localhost") {
    return import.meta.env.VITE_API_SERVER_URL;
  }
  return "/api";
}

const apiClient = axios.create({
  baseURL: getBaseURL(),
  withCredentials: true,
});

let refreshPromise: Promise<void> | null = null;
apiClient.interceptors.response.use(
  (response) => response,
  async (error) => {
    const originalRequest = error.config;

    if (!error.response) return Promise.reject(error);

    if (originalRequest?.url?.includes("/auth/refresh")) {
      return Promise.reject(error);
    }

    // Access Topken 만료 감지
    if (error.response.status === 401 && !originalRequest._retry) {
      originalRequest._retry = true;

      try {
        console.log("Access token 만료. 갱신 시도 중...");

        // 토큰 갱신 수행
        if (!refreshPromise) {
          refreshPromise = apiClient
            .post("/auth/refresh", {})
            .then(() => { })
            .finally(() => {
              refreshPromise = null;
            });
        }

        await refreshPromise;

        console.log("토큰 갱신 성공");
        return apiClient(originalRequest);
      } catch (err) {
        console.log("세션 만료");
        return Promise.reject(err);
      }
    }

    return Promise.reject(error);
  }
);

export default apiClient;
