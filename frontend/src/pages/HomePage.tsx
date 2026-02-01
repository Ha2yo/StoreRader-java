/**
 * File: pages/HomePage.tsx
 * Description:
 *   StoreRader 웹 애플리케이션의 홈 페이지 컴포넌트
 */

import { useEffect, useState } from "react";
import apiClient from "../contexts/apiClient";

function HomePage() {
  const [hello, setHello] = useState<string>("");

  useEffect(() => {
    apiClient.get<string>("/")
      .then(res => setHello(res.data))
      .catch(console.error);
  }, []);

  return (
    <div className="container">
      <h1>Home</h1>
      <p>백엔드 통신 테스트: {hello}</p>
    </div>
  );
}

export default HomePage;