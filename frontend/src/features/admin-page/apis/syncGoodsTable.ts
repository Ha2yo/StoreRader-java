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