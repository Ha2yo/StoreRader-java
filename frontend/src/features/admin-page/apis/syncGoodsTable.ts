/**
 * File: features/admin-page/api/syncGoodsTable.tsx
 * Description:
 *   공공 API로부터 상품 데이터를 가져와
 *   서버의 상품 데이블을 동기화한다
 */

import apiClient from "../../../contexts/apiClient";

export async function syncGoodsTable() {
  try {
    const res = await apiClient.get(
      "/admin/get/public-data/goods"
    );
    console.log("goods API 응답:", res.data);
    
    return res.data;
  } catch (error) {
    console.error("상품 데이터 동기화 실패: ", error);
    throw error;
  }
}