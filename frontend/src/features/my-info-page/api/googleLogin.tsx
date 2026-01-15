import { GoogleLogin } from '@react-oauth/google';
import axios from "axios";

export async function googleLogin() {
  return (
    <div style={{ padding: "20px" }}>
      <h2>로그인 테스트</h2>
      {/* 이 버튼 자체가 화면에 그려져야 하고, 이걸 누르면 팝업이 뜹니다 */}
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

export async function requestBackendLogin() {
  const params = new URLSearchParams(
    window.location.hash.replace("#", "")
  );

  const idToken = params.get("id_token");

  if(!idToken) {
    console.log("idtoken 없음");
    return;
  } 
    const res = await axios.post("/api/auth/google",
      {id_token: idToken,}
    )
  
  console.log("백엔드 응답:", res.data);

}