import useGoodsTable from "../../hooks/select/useGoodsTable";
import { DataTable, type Column } from "./DataTable";
import type { Good, GoodsSortKey } from "../../types/SelectGoods";
import type {  } from "../../types/PageResult";

export default function SelectGoods() {
  const {
    rows,
    totalCount,
    isLoading,
    sortKey,
    sortOrder,
    handleSort,
    page,
    canPrev,
    canNext,
    setPage
  } = useGoodsTable();

  const columns: Column<Good, GoodsSortKey>[] = [
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
    <div>
      <div style={{ display: "flex", gap: 8, alignItems: "center", marginBottom: 12 }}>
        <button disabled={!canPrev} onClick={() => setPage(page - 1)}>이전</button>
        <span>{page + 1}</span>
        <button disabled={!canNext} onClick={() => setPage(page + 1)}>다음</button>
      </div>

      <DataTable<Good, GoodsSortKey>
        title="Goods Table"
        totalCount={totalCount}
        isLoading={isLoading}
        rows={rows}
        columns={columns}
        sortKey={sortKey}
        sortOrder={sortOrder}
        onSort={handleSort}
        rowKey={(g) => g.id}
      />
    </div>
  );
}
