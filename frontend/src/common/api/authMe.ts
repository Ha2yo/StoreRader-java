/**
 * File: common/api/authMe.tsx
 * Description:
 *   현재 로그인한 사용자의 정보를 조회한다
 */

import apiClient from "../../contexts/apiClient";
import type { UserResponse } from "../../features/my-info-page/types/MyInfo.types";

export async function fetchMe() {
  const res = await apiClient.get<UserResponse>("/auth/me");
  return res.data;
}