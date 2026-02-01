/**
 * File: features/my-info-page/components/MyInfo.tsx
 * Description:
 *   사용자 로그인 상태에 따라 로그인 UI 또는 유저 정보 화면을 렌더링하는 컴포넌트
 */

import { GoogleLogin } from "@react-oauth/google";
import { touchEffect } from "../../../common/utils/touchEffect";
import { useMyInfo } from "../hooks/useMyInfo";
import { useAuth } from "../../../contexts/AuthContext";
import { requestGoogleLogin } from "../apis/googleLogin";
import { useNavigate } from "react-router-dom";

export default function MyInfo() {
  const { user, isAdmin, handleLogout, loadHistory, refreshMe } = useMyInfo();
  const { isLoading } = useAuth();

  const navigate = useNavigate();

  console.log("role:", user?.role);

  if (isLoading) {
    return (
      <div className="container" style={{ height: "100vh", display: "flex", justifyContent: "center", alignItems: "center" }}>
        <p style={{ color: "#666" }}>불러오는 중...</p>
      </div>
    );
  }

  // 비로그인 상태
  if (user == null) {
    return (
      <div
        className="container"
        style={{
          height: "100vh",
          display: "flex",
          flexDirection: "column",
          justifyContent: "center",
          alignItems: "center",
        }}
      >
        <br />
        <p style={{ color: "#666" }}>로그인이 필요합니다</p>

        <GoogleLogin
          onSuccess={async (credentialResponse) => {
            try {
              const idToken = credentialResponse.credential as string;
              await requestGoogleLogin(idToken);

              await refreshMe();

            } catch (e) {
              console.error("로그인 처리 실패:", e);
            }
          }}
          onError={() => {
            console.log("구글 로그인 실패");
          }}
        />
      </div>
    );
  }

  // 로그인 상태
  return (
    <div className="container" style={{ paddingTop: "100px" }}>
      <div>
        <img src={user?.picture} alt="profile" className="profile" />
        <p style={{ marginTop: 16, fontSize: 20, fontWeight: "bold" }}>
          {user.name}님 환영합니다
        </p>
        <p style={{ fontSize: 14, color: "#888", marginTop: 4 }}>{user.email}</p>

        {/* 관리자에게만 표시 */}
        {isAdmin && (
          <button
            {...touchEffect}
            style={{
              width: "30%",
              padding: "12px",
              background: "#1890FF",
              borderRadius: "10px",
              color: "#FFFFFF",
              fontSize: "16px",
              fontWeight: "bold",
              marginTop: "20px",
            }}
            onClick={() => navigate('/admin')}
          >
            관리자 페이지
          </button>
        )}

        {/* 사용자 기록 조회 */}
        <button
          {...touchEffect}
          style={{
            width: "80%",
            padding: "12px",
            background: "#eee",
            border: "1px solid #ddd",
            borderRadius: "10px",
            fontSize: "16px",
            marginTop: "20px",
          }}
          onClick={loadHistory}
        >
          기록 보기
        </button>

        {/* 로그아웃 */}
        <button
          {...touchEffect}
          style={{
            width: "80%",
            padding: "12px",
            background: "#FF4D4F",
            color: "#fff",
            border: "none",
            borderRadius: "10px",
            fontSize: "16px",
            marginTop: "12px",
          }}
          onClick={handleLogout}
        >
          로그아웃
        </button>
      </div>
    </div>
  );
}
