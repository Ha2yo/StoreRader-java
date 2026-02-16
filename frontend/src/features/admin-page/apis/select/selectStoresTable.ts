import apiClient from "../../../../contexts/apiClient";
import type { PageResult } from "../../types/PageResult";
import type { Store, StoreSortKey, SortOrder } from "../../types/SelectStores";

export async function selectStoresTable(params:{
  page: number;
  size: number;
  sortKey: StoreSortKey;
  sortOrder: SortOrder;
}): Promise<PageResult<Store>> {
  const res = await apiClient.get("/admin/select/stores", {
    params: {
      page: params.page,
      size: params.size,
      sortKey: params.sortKey,
      sortOrder: params.sortOrder
    }
  });

  const data = res.data as {
    stores: Store[];
    totalCount: number;
    page: number;
    size: number;
  };

  return {
    items: data.stores,
    totalCount: data.totalCount,
    page: data.page,
    size: data.size
  };
}