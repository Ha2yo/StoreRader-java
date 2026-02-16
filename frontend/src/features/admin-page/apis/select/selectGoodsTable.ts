import apiClient from "../../../../contexts/apiClient";
import type { PageResult } from "../../types/PageResult";
import type { Good, GoodsSortKey, SortOrder } from "../../types/SelectGoods";

export async function selectGoodsTable(params:{
  page: number;
  size: number;
  sortKey: GoodsSortKey;
  sortOrder: SortOrder;
}): Promise<PageResult<Good>> {
  const res = await apiClient.get("/admin/select/goods", {
    params: {
      page: params.page,
      size: params.size,
      sortKey: params.sortKey,
      sortOrder: params.sortOrder
    }
  });

  const data = res.data as {
    goods: Good[];
    totalCount: number;
    page: number;
    size: number;
  };

  return {
    items: data.goods,
    totalCount: data.totalCount,
    page: data.page,
    size: data.size
  };
}