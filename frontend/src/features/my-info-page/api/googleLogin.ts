
export async function requestGoogleLogin() {
  const GOOGLE_CLIENT_ID = import.meta.env.VITE_GOOGLE_CLIENT_ID
  const GOOGLE_REDIRECT_URI = import.meta.env.VITE_GOOGLE_REDIRECT_URI

  window.location.href = 
  `https://accounts.google.com/o/oauth2/v2/auth?client_id=${GOOGLE_CLIENT_ID}&redirect_uri=${GOOGLE_REDIRECT_URI}&response_type=token&scope=https://www.googleapis.com/auth/userinfo.email https://www.googleapis.com/auth/userinfo.profile`;
 

  // return ({
  //   clientId: googleClientId,
  //   clientSecret: googleClientPWD,
  //   scopes: ["openid", "email", "profile"],
  // });
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