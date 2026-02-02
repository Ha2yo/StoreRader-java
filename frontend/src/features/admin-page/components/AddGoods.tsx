/**
 * File: features/admin-page/components/AddGoods.tsx
 * Description:
 *   관리자 페이지에서 상품 데이털르 동기화하기 위한 UI 컴포넌트
 */

import { touchEffect } from "../../../common/utils/touchEffect";
import { useSseLogStream } from "../hooks/useSseLogStream";

function AddGoods() {
  const { logs, running, start } = useSseLogStream();

  return (
    <div className="container">
      <button
        {...touchEffect}
        style={{
          width: "20%",
          padding: "12px",
          background: "#1890FF",
          borderRadius: "10px",
          color: "#FFFFFF",
          fontSize: "16px",
          fontWeight: "bold",
          marginTop: "20px",
        }}
        onClick={() => start("/admin/get/public-data/goods")}
      >
        {running ? "동기화 중..." : "데이터 추가"}
      </button>
      <div
        style={{
          marginTop: "16px",
          width: "70%",
          maxWidth: "900px",
          background: "#111",
          color: "#0f0",
          padding: "12px",
          borderRadius: "10px",
          fontFamily: "monospace",
          fontSize: "14px",
          maxHeight: "260px",
          overflowY: "auto",
          whiteSpace: "pre-wrap",
          textAlign: "left",
        }}
      >
        {logs.length === 0 ? (
          <div style={{ color: "#aaa" }}>
            {running ? "로그 수신 대기 중..." : "아직 로그가 없습니다."}
          </div>
        ) : (
          logs.map((line, i) => <div key={i}>{line}</div>)
        )}
      </div>
    </div>
  );
}

export default AddGoods;