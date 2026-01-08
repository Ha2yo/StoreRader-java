import { touchEffect } from "../../../utils/touchEffect";
import { useMyInfo } from "../hooks/useMyInfo";
// import UserHistoryList from "./UseHistory";

export default function MyInfo() {
  const { user, handleLogin, handleLogout, loadHistory } = useMyInfo();

  if (user == null) {
    return (
      <div className="container" style={{
        height: "100vh",
        display: "flex",
        flexDirection: "column",
        justifyContent: "center",
        alignItems: "center",
      }}>
        <br />
        <p style={{ color: "#666" }}>로그인이 필요합니다</p>

        <button
          {...touchEffect}
          style={{
            width: "30%",
            padding: "12px",
            background: "#007aff",
            color: "#fff",
            border: "none",
            borderRadius: "10px",
            fontSize: "16px",
            marginTop: "20px",

          }}
          onClick={handleLogin}>
          로그인
        </button>
      </div>
    );
  }


  return (
    <div className="container" style={{ paddingTop: "100px" }}>
      <div>
        <img
          src={user.picture}
          alt="profile"
          className="profile"
        />
        <p style={{ marginTop: 16, fontSize: 20, fontWeight: "bold" }}>
          {user.name}님 환영합니다
        </p>
        <p style={{ fontSize: 14, color: "#888", marginTop: 4 }}>
          {user.email}
        </p>

        {/* 사용자 매장 선택 로그 조회 */}
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
          onClick={loadHistory}>
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
          onClick={handleLogout}>
          로그아웃
        </button>
      </div>

      {/* 기록이 있을 때만 리스트 표시 */}
      {/* {history.length > 0 && <UserHistoryList history={history} />} */}
    </div>
  );
}