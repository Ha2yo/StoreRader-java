import { useState } from "react";
import { useAuth } from "../../../contexts/AuthContext";
import type { UserHistoryItem } from "../types/MyInfo.types";
import { requestGoogleLogout } from "../api/googleLogout";

export function useMyInfo() {
  const { user, refreshMe, setUser } = useAuth();
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

  return { user, history, refreshMe, handleLogout, loadHistory };
}
