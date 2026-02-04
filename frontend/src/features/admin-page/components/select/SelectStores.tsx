import useStoresTable from "../../hooks/useStoresTable";
import { DataTable, type Column } from "./DataTable";
import type { Store } from "../../types/SelectStores";

type SortKey = "id" | "storeId" | "createdAt" | "updatedAt";

export default function SelectStores() {
  const { stores, sortedStores, isLoading, sortKey, sortOrder, handleSort } =
    useStoresTable();

  const columns: Column<Store, SortKey>[] = [
    { key: "id", header: "ID", sortKey: "id", render: (s) => s.id },
    { key: "goodId", header: "매장 ID", sortKey: "storeId", render: (s) => s.storeId },
    { key: "storeName", header: "매장명", render: (g) => g.storeName },
    { key: "telNo", header: "전화번호", render: (g) => g.telNo },
    { key: "postNo", header: "우편번호", render: (g) => g.postNo },
    { key: "jibunAddr", header: "지번주소", render: (g) => g.jibunAddr },
    { key: "roadAddr", header: "도로명주소", render: (g) => g.roadAddr },
    { key: "lat", header: "위도", render: (g) => g.lat },
    { key: "lng", header: "경도", render: (g) => g.lng },
    { key: "areaCode", header: "지역코드", render: (g) => g.areaCode },
    { key: "areaDetailCode", header: "지역상세코드", render: (g) => g.areaDetailCode },
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
      totalCount={stores?.length ?? 0}
      isLoading={isLoading}
      rows={sortedStores}
      columns={columns}
      sortKey={sortKey}
      sortOrder={sortOrder}
      onSort={handleSort}
      rowKey={(g) => g.id}
    />
  );
}