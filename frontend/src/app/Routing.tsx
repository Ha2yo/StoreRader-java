import { BrowserRouter, Route, Routes } from "react-router-dom";
import HomePage from "../pages/Home";
import MyPage from "../pages/MyInfo";
import MapPage from "../pages/Map";
import SearchPage from "../pages/Search";
import MaintenancePage from "../pages/Maintenance";
import App from "./App";
import Navigation from "./Navigation";
import { AuthProvider } from "../contexts/AuthContext";

function Routing() {
    return (
        <AuthProvider>
            <BrowserRouter>
                <RoutingContents />
            </BrowserRouter>
        </AuthProvider>
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
            </Routes>
        </>
    );
}

export default Routing;