/**
 * File: app/App.tsx
 * Description:
 *   StoreRader 웹 애플리케이션의 최상위 엔트리 컴포넌트
 *
 * Responsibilities:
 *   1. 앱 최초 진입 시 기본 라우트('/home')로 리다이렉트
 */

import { useEffect } from "react";
import { useNavigate } from "react-router-dom";

export function App() {
    const navigate = useNavigate();

    useEffect(() => {
        navigate("/home");
    }, []);

    return (
        <div className='container'>
        </div>
    );
}

export default App;