import apiClient from "../../../contexts/apiClient";
import type { Store } from "../types/StoreItem";
import type { StoreResponse } from "../types/StoresResponse";

export async function findAllStore(): Promise<Store[]> {
  try {
    const res = await apiClient.get<StoreResponse>(
      "/find/store/all"
    );
    return res.data.stores;
  } catch (error) {
    console.error("매장 테이블 조회 실패: ", error);
    throw error;
  }
}