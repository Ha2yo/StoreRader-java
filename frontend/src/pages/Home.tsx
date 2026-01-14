import axios from "axios";
import { useEffect, useState } from "react";

function Home() {
  const API_SERVER_URL = import.meta.env.VITE_API_SERVER_URL
  const [hello, setHello] = useState([]);

  useEffect(() => {
    axios.get(API_SERVER_URL)
      .then(response => setHello(response.data))
      .catch(error => console.log(error))
  }, []);

  return (
    <div className='container'>
      <h1>Home</h1>
      <p> 백엔드 통신 테스트: {hello.map((text, index) => <li key={`${index}-${text}`}>{text}</li>)} </p>
    </div>
  );
}

export default Home;
