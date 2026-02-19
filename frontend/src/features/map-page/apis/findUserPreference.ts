import apiClient from "../../../contexts/apiClient";
import type { UserPreference } from "../types/UserPreference";

export async function findUserPreference(): Promise<UserPreference> {
  const DEFAULT_PREFERENCE: UserPreference = {
    distanceWeight: 0.5,
    priceWeight: 0.5
  };

  try {
    const res = await apiClient.get<UserPreference>(
      "/find/user-preference"
    );
    console.log("로그인 사용자 - 개별값 사용");
    return res.data;
  } catch (error: any) {
    if (error.response.status === 400) {
      console.log("비로그인 사용자 - 기본값 사용");

      return DEFAULT_PREFERENCE;
    }
    console.error("가중치 조회 실패: ", error);
    return DEFAULT_PREFERENCE;
  }
}