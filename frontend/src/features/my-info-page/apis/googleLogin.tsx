/**
 * File: features/my-info-page/api/requestGoogleLogin.tsx
 * Description:
 *   Google ID Token을 이용해 백엔드에 로그인을 요청한다
 */

import apiClient from "../../../contexts/apiClient";
import type { GoogleLoginResponse } from '../types/MyInfo.types';

export async function requestGoogleLogin(idToken: string) {
  try {
    const res = await apiClient.post<GoogleLoginResponse>(
      "/auth/google",
      { idToken }
    );

    console.log("백엔드 응답:", res.data.user);
    return res.data;

  } catch (error) {
    console.error("백엔드 로그인 요청 실패: ", error);
    throw error;
  }
}