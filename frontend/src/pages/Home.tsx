import axios from "axios";
import { useEffect, useState } from "react";

function Home() {
  const API_SERVER_URL = import.meta.env.VITE_API_SERVER_URL;
  const [hello, setHello] = useState<string>("");

  console.log("backend 주소: ",API_SERVER_URL);

  useEffect(() => {
    axios.get<string>(API_SERVER_URL)
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