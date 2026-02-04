import useGoodsTable from "../../hooks/select/useGoodsTable";
import { DataTable, type Column } from "./DataTable";
import type { Good } from "../../types/SelectGoods";

type SortKey = "id" | "goodId" | "createdAt" | "updatedAt";

export default function SelectGoods() {
  const { goods, sortedGoods, isLoading, sortKey, sortOrder, handleSort } =
    useGoodsTable();

  const columns: Column<Good, SortKey>[] = [
    { key: "id", header: "ID", sortKey: "id", render: (g) => g.id },
    { key: "goodId", header: "상품 ID", sortKey: "goodId", render: (g) => g.goodId },
    { key: "goodName", header: "상품명", render: (g) => g.goodName },
    { key: "totalCnt", header: "총 용량", render: (g) => g.totalCnt },
    { key: "totalDivCode", header: "분류 코드", render: (g) => g.totalDivCode },
    {
      key: "createdAt",
      header: "생성일자",
      sortKey: "createdAt",
      render: (g) => new Date(g.createdAt).toLocaleDateString(),
    },
    {
      key: "updatedAt",
      header: "수정일자",
      sortKey: "updatedAt",
      render: (g) =>
        g.updatedAt ? new Date(g.updatedAt).toLocaleString() : "기록 없음",
    },
  ];

  return (
    <DataTable
      title="Goods Table"
      totalCount={goods?.length ?? 0}
      isLoading={isLoading}
      rows={sortedGoods}
      columns={columns}
      sortKey={sortKey}
      sortOrder={sortOrder}
      onSort={handleSort}
      rowKey={(g) => g.id}
    />
  );
}
