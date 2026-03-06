import apiClient from "../../../contexts/apiClient";
import type { StorePrice } from "../types/StorePriceItem";
``
export async function findPrice(goodName: string): Promise<StorePrice[]> {
  try {
    const res = await apiClient.get(
      "/find/price",
      {
        params: { "good-name": goodName }
      }
    );
    console.log("res.data =", res.data);
    return res.data ?? [];
    
  } catch (error) {
    console.error("가격 테이블 조회 실패: ", error);
    throw error;
  }
}