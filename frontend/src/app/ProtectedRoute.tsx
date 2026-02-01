/**
 * File: app/ProtectedRoute.tsx
 * Description:
 *   관리자 권한이 필요한 라우트를 보호하기 위한 컴포넌트
 *
 * Responsibilities:
 *   1. 현재 로그인 상태 및 관리자 권한 여부 확인
 *   2. 관리자 권한이 없을 경우 홈 화면으로 리다이렉트
 */

import { Navigate, Outlet } from 'react-router-dom';
import { useAuth } from '../contexts/AuthContext';

const ProtectedRoute = () => {
  const { isAdmin, isLoading, user } = useAuth();

  if (isLoading) {
    return <div>권한 확인 중...</div>;
  }

  if (!user || !isAdmin) {
    alert("관리자 권한이 필요합니다.");
    return <Navigate to="/home" replace />;
  }

  return <Outlet />;
};

export default ProtectedRoute;