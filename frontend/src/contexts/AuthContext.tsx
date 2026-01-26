import React, { createContext, useContext, useEffect, useState } from "react";
import { fetchMe } from "../common/api/authMe";
import type { UserResponse } from "../features/my-info-page/types/MyInfo.types";

type AuthContextValue = {
  user: UserResponse | null;
  isLoading: boolean;
  refreshMe: () => Promise<void>;
  setUser: (u: UserResponse | null) => void;
};

const AuthContext = createContext<AuthContextValue | null>(null);

export function AuthProvider({ children }: { children: React.ReactNode }) {
  const [user, setUser] = useState<UserResponse | null>(null);
  const [isLoading, setIsLoading] = useState(true);

  async function refreshMe() {
    try {
      const me = await fetchMe();
      setUser(me);
    } catch {
      setUser(null);
    }
  }

  useEffect(() => {
    setIsLoading(true);
    refreshMe().finally(() => setIsLoading(false));
  }, []);

  const value: AuthContextValue = { user, isLoading, refreshMe, setUser };

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
