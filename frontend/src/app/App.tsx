import { useEffect } from "react";
import { useNavigate } from "react-router-dom";

function App() {
    const navigate = useNavigate();

    useEffect(() => {
        // 컴포넌트가 마운트된 직후에 이동하도록 변경
        navigate("/home");
    }, []);

    return (
        <div className='container'>
        </div>
    );
}

export default App;