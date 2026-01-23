import axios from "axios";
import type { GoogleLoginResponse } from '../types/MyInfo.types';

export async function requestBackendLogin(idToken: string) {
  try {
    const res = await axios.post<GoogleLoginResponse>(
      "/api/auth/google",
      { idToken },
      { withCredentials: true }
    );

    console.log("백엔드 응답:", res.data);
    return res.data;
  } catch (error) {
    console.error("백엔드 로그인 요청 실패: ", error);
  }


}