import apiClient from "../../../../contexts/apiClient";

export async function selectStoresTable() {
  try {
    const res = await apiClient.get(
      "/admin/select/stores"
    );

    return res.data.goods;
  } catch (error) {
    console.error("매장 테이블 조회 실패: ", error);
    throw error;
  }
}