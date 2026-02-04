import { touchEffect } from "../../../../common/utils/touchEffect";
import { useSseLogStream } from "../../hooks/useSseLogStream";

type SseLogProps = {
    endpoint: string;
}

function SseLog({endpoint}: SseLogProps) {
    const { logs, running, start } = useSseLogStream();

    return (
        <>
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
                onClick={() => start(endpoint)}
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
                    maxHeight: "350px",
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
            </>
    );
}
export default SseLog;