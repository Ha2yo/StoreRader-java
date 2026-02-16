import apiClient from "../../../../contexts/apiClient";
import type { PageResult } from "../../types/PageResult";
import type { RegionCode, RegionCodeSortKey, SortOrder } from "../../types/SelectRegionCodes";

export async function selectRegionCodesTable(params:{
  page: number;
  size: number;
  sortKey: RegionCodeSortKey;
  sortOrder: SortOrder;
}): Promise<PageResult<RegionCode>> {
  const res = await apiClient.get("/admin/select/region-codes", {
    params: {
      page: params.page,
      size: params.size,
      sortKey: params.sortKey,
      sortOrder: params.sortOrder
    }
  });

  const data = res.data as {
    regionCodes: RegionCode[];
    totalCount: number;
    page: number;
    size: number;
  };

  return {
    items: data.regionCodes,
    totalCount: data.totalCount,
    page: data.page,
    size: data.size
  };
}