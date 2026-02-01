/**
 * File: app/Routing.tsx
 * Description:
 *   StoreRader 웹 에플리케이션의 라우팅 엔트리
 *
 * Responsibilities:
 *   1. GoogleOAuthProvider, AuthProvider로 전역 컨텍스트 주입
 *   2. Routes 구성
 *   3. 관리자 전용 라우트에 ProtectedRoute 적용
 */

import { BrowserRouter, Route, Routes } from "react-router-dom";
import HomePage from "../pages/HomePage";
import MyPage from "../pages/MyInfoPage";
import MapPage from "../pages/MapPage";
import SearchPage from "../pages/SearchPage";
import App from "./App";
import Navigation from "./Navigation";
import { AuthProvider } from "../contexts/AuthContext";
import { GoogleOAuthProvider } from "@react-oauth/google";
import Admin from "../pages/Adminpage";
import ProtectedRoute from "./ProtectedRoute";

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

function RoutingContents() {
    return (
        <>
            <Navigation />

            <Routes>
                {/* 공개 페이지 */}
                <Route path='/' element={<App />} />
                <Route path='/home' element={<HomePage />} />
                <Route path='/map' element={<MapPage />} />
                <Route path='/my-info' element={<MyPage />} />
                <Route path='/search' element={<SearchPage />} />

                {/* 관리자 전용 페이지 */}
                <Route element={<ProtectedRoute />}>
                    <Route path='/admin' element={<Admin />} />
                </Route>
            </Routes>
        </>
    );
}

export default Routing;