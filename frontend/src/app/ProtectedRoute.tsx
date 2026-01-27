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