import { useEffect } from "react";
import { useNavigate } from "react-router-dom";

function App() {
    const navigate = useNavigate();

    localStorage.removeItem("lastSearchTerm");
    localStorage.removeItem("selectedGoodName");

    useEffect(() => {
        const tryNavigate = async () => {
            // 오프라인 상태에서는 초기 이동 중단

            // 서버 상태가 정상일 때만 홈 화면으로 이동
            if (status === "ok") {
                localStorage.removeItem("lastSearchTerm");
                localStorage.removeItem("selectedGoodName");
                navigate("/home");
            }
        };

        tryNavigate();
    });

    return (
        <div className='container'>
        </div>
    );
}

export default App;