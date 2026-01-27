import { BrowserRouter, Route, Routes } from "react-router-dom";
import HomePage from "../pages/HomePage";
import MyPage from "../pages/MyInfoPage";
import MapPage from "../pages/MapPage";
import SearchPage from "../pages/SearchPage";
import MaintenancePage from "../pages/MaintenancePage";
import App from "./App";
import Navigation from "./Navigation";
import { AuthProvider } from "../contexts/AuthContext";
import { GoogleOAuthProvider } from "@react-oauth/google";
import Admin from "../pages/Adminpage";

function Routing() {
    const GOOGLE_CLIENT_ID = import.meta.env.VITE_GOOGLE_CLIENT_ID

    return (
        <GoogleOAuthProvider clientId={GOOGLE_CLIENT_ID}>
            <AuthProvider>
                <BrowserRouter>
                    <RoutingContents />
                </BrowserRouter>
            </AuthProvider>
        </GoogleOAuthProvider>
    );
}

/**
 * 라우팅 처리 + 네트워크 상태 감지 + 페이지별 네비게이션 표시 제어 담당
 */
function RoutingContents() {
    return (
        <>
            <Navigation />

            {/* 애플리케이션 라우트 구성 */}
            <Routes>
                <Route path='/' element={<App />} />
                <Route path='/home' element={<HomePage />} />
                <Route path='/map' element={<MapPage />} />
                <Route path='/my-info' element={<MyPage />} />
                <Route path='/search' element={<SearchPage />} />
                <Route path='/maintenance' element={<MaintenancePage />} />
                <Route path='/admin' element={<Admin />} />
            </Routes>
        </>
    );
}

export default Routing;