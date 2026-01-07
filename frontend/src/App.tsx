import { useEffect } from "react";
import { useNavigate } from "react-router-dom";

function App() {
    const navigate = useNavigate();

    localStorage.removeItem("lastSearchTerm");
    localStorage.removeItem("selectedGoodName");

    useEffect(() => {
        const tryNavigate = async () => {

            // 서버 상태가 정상일 때만 홈 화면으로 이동
            if (status === "ok") {
                navigate("/home");
            }
        };

        tryNavigate();
    });

    return (
        <div className='container'>
            <h1>초기화면</h1>
        </div>
    );
}

export default App;