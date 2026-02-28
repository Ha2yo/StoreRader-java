import apiClient from "../../../../contexts/apiClient";
import type { SelectionPayload } from "../types/SelectionPayload";

export async function insertUserSelection(payload: SelectionPayload) {
  try {
    await apiClient.post(
      "/insert/user-selection", payload
        );
    console.log("로그인 사용자 - 매장 선택 로그 기록");

  } catch (error: any) {
    if (error.response.status === 400) {
      console.log("비로그인 사용자 - 로그 저장 스킵");

    }
    console.error("매장 선택 로그 저장 실패: ", error);
  }
} 