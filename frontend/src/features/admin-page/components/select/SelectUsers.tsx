import useUsersTable from "../../hooks/select/useUsersTable";
import { DataTable, type Column } from "./DataTable";
import type { User, UsersSortKey } from "../../types/SelectUsers";
import Pagination from "./Pagination";

export default function SelectUsers() {
  const {
    rows,
    totalCount,
    isLoading,
    sortKey,
    sortOrder,
    handleSort,
    page,
    size,
    setPage
  } = useUsersTable();

   const columns: Column<User, UsersSortKey>[] = [
    { key: "id", header: "ID", sortKey: "id", render: (u) => u.id },
    { key: "role", header: "권한", sortKey: "role", render: (u) => u.role },
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
    <div>
      <DataTable<User, UsersSortKey>
        title="Users Table"
        totalCount={totalCount}
        isLoading={isLoading}
        rows={rows}
        columns={columns}
        sortKey={sortKey}
        sortOrder={sortOrder}
        onSort={handleSort}
        rowKey={(u) => u.id}
      />
      <Pagination 
        page={page}
        size={size}
        totalCount={totalCount}
        onPageChange={setPage}
        groupSize={10}/>
    </div>
  );
}
