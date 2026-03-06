import apiClient from "../../../../contexts/apiClient";

export async function findAllRegionCodes() {
  try {
    const res = await apiClient.get(
      "/find/region-code/all"
    );
    console.log(res.data);
    return res.data;
  } catch (error) {
    console.error("지역코드 테이블 조회 실패: ", error);
    throw error;
  }
}