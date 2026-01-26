import apiClient from "../../../contexts/apiClient";
import { useAuth } from "../../../contexts/AuthContext";
import type { GoogleLoginResponse } from '../types/MyInfo.types';

export async function requestBackendLogin(idToken: string) {
  const {user} = useAuth();
  try {
    const res = await apiClient.post<GoogleLoginResponse>(
      "/auth/google",
      { idToken }
    );

    console.log("백엔드 응답:", res.data.user);
    console.log("role: ", user?.role);
    return res.data;

  } catch (error) {
    console.error("백엔드 로그인 요청 실패: ", error);
    throw error;
  }
}