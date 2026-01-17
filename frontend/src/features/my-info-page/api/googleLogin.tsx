import { GoogleLogin } from '@react-oauth/google';
import axios from "axios";

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
    const res = await axios.post<boolean>("/api/auth/google",
      {idToken,}
    );
  
  console.log("백엔드 응답:", res.data);

}