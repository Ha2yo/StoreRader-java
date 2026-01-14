import axios from "axios";
import { useEffect, useState } from "react";

function Home() {
  const [hello, setHello] = useState<string>("");

  useEffect(() => {
    axios.get<string>("/api")
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

export default Home;