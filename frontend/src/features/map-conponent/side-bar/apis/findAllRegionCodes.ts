import apiClient from "../../../../contexts/apiClient";
import type { RegionCodeResponse } from "../types/RegionCodeResponse";

export async function findAllRegionCodes() {
  try {
    const res = await apiClient.get<RegionCodeResponse>(
      "/find/region-code/all"
    );
    console.log(res.data);
    return res.data.regionCodes;
  } catch (error) {
    console.error("지역코드 테이블 조회 실패: ", error);
    throw error;
  }
}