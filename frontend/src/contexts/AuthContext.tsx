/**
 * File: contexts/AuthContext.tsx
 * Description:
 *   StoreRader 웹 에플리케이션의 인증 상태를 전역으로 관리하는 컨텍스트
 *
 * Responsibilities:
 *   1. 앱 최초 진입 시 서버를 기준으로 인증 상태 복구
 */

import React, { createContext, useContext, useEffect, useState } from "react";
import { fetchMe } from "../common/api/authMe";
import type { UserResponse } from "../features/my-info-page/types/MyInfo.types";

type AuthContextValue = {
  user: UserResponse | null;
  isAdmin: boolean;
  isLoading: boolean;
  refreshMe: () => Promise<void>;
  setUser: (u: UserResponse | null) => void;
};

const AuthContext = createContext<AuthContextValue | null>(null);

export function AuthProvider({ children }: { children: React.ReactNode }) {
  const [user, setUser] = useState<UserResponse | null>(null);  // 사용자 정보
  const [isLoading, setIsLoading] = useState(true);             // 인증 상태 로딩 여부

  const isAdmin = user?.role === "ADMIN";

  // 현재 로그인한 사용자 정보 갱신
  async function refreshMe() {
    try {
      const me = await fetchMe();
      setUser(me);
    } catch {
      setUser(null);
    }
  }

  // 앱 최초 마운트 시 로그인 상태 복구
  useEffect(() => {
    setIsLoading(true);
    refreshMe().finally(() => setIsLoading(false));
  }, []);

  const value: AuthContextValue = { user, isAdmin, isLoading, refreshMe, setUser };

  return (
    <AuthContext.Provider value={value}>
      {children}
    </AuthContext.Provider>
  );
}

export function useAuth() {
  const ctx = useContext(AuthContext);
  if (!ctx) throw new Error("useAuth must be used within AuthProvider");
  return ctx;
}
