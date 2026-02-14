import apiClient from "../../../contexts/apiClient";
import type { Good } from "../types/GoodItem";
import type { GoodResponse } from "../types/GoodsResponse";

export async function findAllGood(): Promise<Good[]> {
  try {
    const res = await apiClient.get<GoodResponse>(
      "/find/good/all"
    );
    return res.data.goods;
  } catch (error) {
    console.error("상품 테이블 조회 실패: ", error);
    throw error;
  }
}