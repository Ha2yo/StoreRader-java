/**
 * File: features/my-info-page/hooks/useMyInfo.tsx
 * Description:
 *   마이페이지에서 사용하는 유저 정보를 관리한다
 */

import { useState } from "react";
import { useAuth } from "../../../contexts/AuthContext";
import type { UserHistoryItem } from "../types/MyInfo.types";
import { requestGoogleLogout } from "../apis/googleLogout";

export function useMyInfo() {
  const { user, isAdmin, refreshMe, setUser } = useAuth();
  const [history, setHistory] = useState<UserHistoryItem[]>([]);

  async function handleLogout() {
    try {
      await requestGoogleLogout();
    } finally {
      setUser(null);
      setHistory([]);
    }
  }

  async function loadHistory() {

  }

  return { user, isAdmin, history, refreshMe, handleLogout, loadHistory };
}
