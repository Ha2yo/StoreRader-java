import apiClient from "../../../../contexts/apiClient";

export async function selectGoodsTable() {
  try {
    const res = await apiClient.get(
      "/admin/select/goods"
    );
    console.log(res.data.goods);
    return res.data.goods;
  } catch (error) {
    console.error("상품 테이블 조회 실패: ", error);
    throw error;
  }
}