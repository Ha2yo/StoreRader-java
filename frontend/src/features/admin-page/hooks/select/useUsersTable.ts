/**
 * File: features/admin-page/hooks/useUserTable.tsx
 * Description:
 *   관리자 페이지에서 유저 목록을 조회하고 정렬 상태를 관리한다
 */

import type { User } from "../../types/SelectUsers";
import { selectUsersTable } from "../../apis/select/selectUsersTable";
import { useSortableTable } from "../useSortableTable";

type SortKey = "id" | "createdAt" | "lastLogin";

export default function useGoodsTable() {
  const table = useSortableTable<User, SortKey>({
    fetcher: selectUsersTable,
    initialSortKey: "id",
    getSortValue: (u, key) => {
      switch (key) {
        case "id":
          return u.id;
        case "createdAt":
          return new Date(u.createdAt).getTime();
        case "lastLogin":
          return u.lastLogin ? new Date(u.lastLogin).getTime() : 0;
      }
    },
  });

  return {
    users: table.rows,
    sortedUsers: table.sortedRows,
    isLoading: table.isLoading,
    sortKey: table.sortKey,
    sortOrder: table.sortOrder,
    handleSort: table.handleSort,
    setUsers: table.setRows,
  };
}