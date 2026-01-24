import React, { createContext, useContext, useEffect, useMemo, useState } from "react";
import { fetchMe } from "../features/my-info-page/api/authMe";
import type { UserResponse } from "../features/my-info-page/types/MyInfo.types";

type AuthContextValue = {
  user: UserResponse | null;
  isLoading: boolean;
  refreshMe: () => Promise<void>;
  setUser: (u: UserResponse | null) => void;
  logoutLocal: () => void;
};

const AuthContext = createContext<AuthContextValue | null>(null);

export function AuthProvider({ children }: { children: React.ReactNode }) {
  const [user, setUser] = useState<UserResponse | null>(null);
  const [isLoading, setIsLoading] = useState(true);

  async function refreshMe() {
    try {
      const me = await fetchMe();
      const picture = localStorage.getItem("userPicture");
      setUser({
        ...me,
        picture: me.picture ?? picture,
      });
    } catch {
      setUser(null);
    }
  }

  function logoutLocal() {
    setUser(null);
  }

  useEffect(() => {
    (async () => {
      setIsLoading(true);
      await refreshMe();
      setIsLoading(false);
    })();
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, []);

  const value = useMemo(
    () => ({ user, isLoading, refreshMe, setUser, logoutLocal }),
    [user, isLoading]
  );

  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
}

export function useAuth() {
  const ctx = useContext(AuthContext);
  if (!ctx) throw new Error("useAuth must be used within AuthProvider");
  return ctx;
}
