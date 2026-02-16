import apiClient from "../../../../contexts/apiClient";
import type { PageResult } from "../../types/PageResult";
import type { User, UserSortKey, SortOrder } from "../../types/SelectUsers";

export async function selectUsersTable(params:{
  page: number;
  size: number;
  sortKey: UserSortKey;
  sortOrder: SortOrder;
}): Promise<PageResult<User>> {
  const res = await apiClient.get("/admin/select/users", {
    params: {
      page: params.page,
      size: params.size,
      sortKey: params.sortKey,
      sortOrder: params.sortOrder
    }
  });

  const data = res.data as {
    users: User[];
    totalCount: number;
    page: number;
    size: number;
  };

  return {
    items: data.users,
    totalCount: data.totalCount,
    page: data.page,
    size: data.size
  };
}