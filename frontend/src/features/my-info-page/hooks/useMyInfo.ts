import { useState } from "react";
import { useAuth } from "../../../contexts/AuthContext";
// import { fetchUserHistory } from "../api/fetchUserHistory";
import { googleLogin, requestBackendLogin } from "../api/googleLogin";
import { requestGoogleLogout } from "../api/googleLogout";
import type { UserHistoryItem } from "../types/MyInfo.types";

export function useMyInfo() {
  const { user, logout } = useAuth();
  const [history, setHistory] = useState<UserHistoryItem[]>([]);

  // Google OAuth → 백엔드 인증 → JWT 발급
  async function handleLogin() {
    try {
      const googleRes = await googleLogin();

      // if (googleRes.idToken) {
      //   const serverRes = await requestBackendLogin(googleRes.idToken);
      //   console.log("로그인 성공", serverRes);
      // }
    } catch (error) {
      console.error("로그인 중 오류 발생", error);
    } 
  }

  async function handleLogout() {
    await requestGoogleLogout();
    logout();
    setHistory([]); // 로그아웃 시 히스토리 초기화
  }

  // 사용자 매장 선택 로그 요청
  async function loadHistory() {
    // const jwt = localStorage.getItem("jwt");
    // const data = await fetchUserHistory(jwt);
    // setHistory(data);
  }

  return { user, history, handleLogin, handleLogout, loadHistory };
}