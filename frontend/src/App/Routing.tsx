import { BrowserRouter, Route, Routes } from "react-router-dom";
import HomePage from "../pages/Home.tsx";
import MyPage from "../pages/MyInfo.tsx";
import MapPage from "../pages/Map.ts";
import SearchPage from "../pages/Search.tsx";
import MaintenancePage from "../pages/Maintenance.tsx";
import App from "../App";
import Navigation from "./Navigation.tsx";

function Routing() {
    return (
        <BrowserRouter>
            <RoutingContents />
        </BrowserRouter>
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
                <Route path='/myInfo' element={<MyPage />} />
                <Route path='/search' element={<SearchPage />} />
                <Route path='/maintenance' element={<MaintenancePage />} />
            </Routes>
        </>
    );
}

export default Routing;