import apiClient from "../../../../contexts/apiClient";

export async function selectPricesTable() {
  try {
    const res = await apiClient.get(
      "/admin/select/prices"
    );
    console.log(res.data.prices);
    return res.data.prices;
  } catch (error) {
    console.error("가격 테이블 조회 실패: ", error);
    throw error;
  }
}