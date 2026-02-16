import apiClient from "../../../../contexts/apiClient";
import type { PageResult } from "../../types/PageResult";
import type { Price, PriceSortKey, SortOrder } from "../../types/SelectPrices";

export async function selectPricesTable(params:{
  page: number;
  size: number;
  sortKey: PriceSortKey;
  sortOrder: SortOrder;
}): Promise<PageResult<Price>> {
  const res = await apiClient.get("/admin/select/prices", {
    params: {
      page: params.page,
      size: params.size,
      sortKey: params.sortKey,
      sortOrder: params.sortOrder
    }
  });

  const data = res.data as {
    prices: Price[];
    totalCount: number;
    page: number;
    size: number;
  };

  return {
    items: data.prices,
    totalCount: data.totalCount,
    page: data.page,
    size: data.size
  };
}