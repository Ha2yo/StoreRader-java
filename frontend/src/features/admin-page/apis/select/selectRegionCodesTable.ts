import apiClient from "../../../../contexts/apiClient";

export async function selectRegionCodesTable() {
  try {
    const res = await apiClient.get(
      "/admin/select/regionCodes"
    );
    console.log(res.data.goods);
    return res.data.regionCodes;
  } catch (error) {
    console.error("지역보드 테이블 조회 실패: ", error);
    throw error;
  }
}