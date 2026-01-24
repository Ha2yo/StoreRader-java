import apiClient from "../../../contexts/apiClient";
import type { UserResponse } from "../types/MyInfo.types";

export async function fetchMe() {
  const res = await apiClient.get<UserResponse>("/auth/me");
  return res.data;
}