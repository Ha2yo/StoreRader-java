import { GoogleLogin } from '@react-oauth/google';
import axios from "axios";
import type { GoogleLoginResponse } from '../types/MyInfo.types';

export async function googleLogin() {
  return (
    <div style={{ padding: "20px" }}>
      <h2>로그인 테스트</h2>
      <GoogleLogin
        onSuccess={(credentialResponse) => {
          console.log("로그인 성공! 생성된 토큰:", credentialResponse.credential);

        }}
        onError={() => {
          console.log('로그인 실패');
        }}
      />
    </div>
  );
}

export async function requestBackendLogin(idToken: string) {
  try {
    const res = await axios.post<GoogleLoginResponse>("/api/auth/google",
      { idToken: idToken }
    );

    console.log("백엔드 응답:", res.data);
    return res.data;
  } catch (error) {
    console.error("백엔드 로그인 요청 실패: ", error);
  }


}