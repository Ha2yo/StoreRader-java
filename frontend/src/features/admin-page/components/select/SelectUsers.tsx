/**
 * File: features/admin-page/components/SelectUsers.tsx
 * Description:
 *   관리자 페이지에서 유저 목록을 테이블 형태로 표시하는 UI 컴포넌트
 */

import useUsersTable from "../../hooks/select/useUsersTable";
import { DataTable, type Column } from "./DataTable";
import type { User } from "../../types/SelectUsers";

type SortKey = "id" | "createdAt" | "lastLogin";

export default function SelectUsers() {
  const { users, sortedUsers, isLoading, sortKey, sortOrder, handleSort } =
    useUsersTable();

  const columns: Column<User, SortKey>[] = [
    { key: "id", header: "ID", sortKey: "id", render: (u) => u.id },
    { key: "name", header: "유저 이름", render: (u) => u.name },
    { key: "email", header: "유저 이메일", render: (u) => u.email },
    {
      key: "createdAt",
      header: "생성일자",
      sortKey: "createdAt",
      render: (u) => new Date(u.createdAt).toLocaleDateString(),
    },
    {
      key: "updatedAt",
      header: "수정일자",
      sortKey: "lastLogin",
      render: (u) =>
        u.lastLogin ? new Date(u.lastLogin).toLocaleString() : "기록 없음",
    },
  ];

  return (
    <DataTable
      title="Users Table"
      totalCount={users?.length ?? 0}
      isLoading={isLoading}
      rows={sortedUsers}
      columns={columns}
      sortKey={sortKey}
      sortOrder={sortOrder}
      onSort={handleSort}
      rowKey={(u) => u.id}
    />
  );
}