import { useRef, useState } from "react";

type UseSseLogStreamResult = {
  logs: string[];
  running: boolean;
  start: (path: string) => void;
  stop: () => void;
};

export function useSseLogStream(): UseSseLogStreamResult {
  const [logs, setLogs] = useState<string[]>([]);
  const [running, setRunning] = useState(false);
  const esRef = useRef<EventSource | null>(null);

  const stop = () => {
    esRef.current?.close();
    esRef.current = null;
    setRunning(false);
  };

  const start = (path: string) => {
    if (running) return;

    setLogs([]);
    setRunning(true);

    const baseURL = getApiBaseURL();
    const url =
      baseURL.endsWith("/")
        ? `${baseURL.slice(0, -1)}${path}`
        : `${baseURL}${path}`;

    const es = new EventSource(url, { withCredentials: true });

    es.addEventListener("log", (e) => {
      setLogs((prev) => [...prev, (e as MessageEvent).data]);
    });

    es.addEventListener("done", () => {
      stop();
    });

    es.onerror = () => {
      setLogs((prev) => [...prev, "오류: 스트림 연결이 끊겼습니다."]);
      stop();
    };

    esRef.current = es;
  };

  return { logs, running, start, stop };
}

export const getApiBaseURL = () => {
  const { hostname } = window.location;

  if (hostname === "localhost") {
    return import.meta.env.VITE_API_SERVER_URL;
  }

  return "/api";
};
