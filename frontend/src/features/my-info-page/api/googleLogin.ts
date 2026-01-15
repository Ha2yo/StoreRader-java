export async function requestGoogleLogin() {
  const GOOGLE_CLIENT_ID = import.meta.env.VITE_GOOGLE_CLIENT_ID
  const GOOGLE_REDIRECT_URI = import.meta.env.VITE_GOOGLE_REDIRECT_URI

  const nonce = Math.random().toString(36).substring(2);

  window.location.href = 
  `https://accounts.google.com/o/oauth2/v2/auth?`+
  `client_id=${GOOGLE_CLIENT_ID}&`+
  `redirect_uri=${GOOGLE_REDIRECT_URI}&`+
  `response_type=id_token&`+
  `scope=openid https://www.googleapis.com/auth/userinfo.email https://www.googleapis.com/auth/userinfo.profile&`+
  `nonce=${nonce}`;
}

export function printGoogleIdTokenFromUrl() {
  if (!window.location.hash) return;

  const params = new URLSearchParams(
    window.location.hash.replace("#", "")
  );

  const idToken = params.get("id_token");

  if (idToken) {
    console.log("ID TOKEN:", idToken);
  } else {
    console.log("id_token 없음");
  }
}

// export async function requestBackendLogin(idToken: string) {
//   const apiURL = await invoke<string>("c_get_env_value", { name: "API_URL" });

//   const res = await fetch(`${apiURL}/auth/google`, {
//     method: "POST",
//     headers: { "Content-Type": "application/json" },
//     body: JSON.stringify({
//       id_token: idToken,
//       client_id: await invoke("c_get_env_value", { name: "GOOGLE_CLIENT_ID" }),
//     }),
//   });

//   if (!res.ok) throw new Error("백엔드 로그인 실패");

//   return res.json();
// }