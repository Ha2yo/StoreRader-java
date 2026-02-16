import useStoresTable from "../../hooks/select/useStoresTable";
import { DataTable, type Column } from "./DataTable";
import type { Store, StoreSortKey } from "../../types/SelectStores";
import Pagination from "./Pagination";

export default function SelectStores() {
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
  } = useStoresTable();

   const columns: Column<Store, StoreSortKey>[] = [
    { key: "id", header: "ID", sortKey: "id", render: (s) => s.id },
    { key: "goodId", header: "매장 ID", sortKey: "storeId", render: (s) => s.storeId },
    { key: "storeName", header: "매장명", render: (s) => s.storeName },
    { key: "telNo", header: "전화번호", render: (s) => s.telNo },
    { key: "postNo", header: "우편번호", render: (s) => s.postNo },
    { key: "jibunAddr", header: "지번주소", render: (s) => s.jibunAddr },
    { key: "roadAddr", header: "도로명주소", render: (s) => s.roadAddr },
    { key: "lat", header: "위도", render: (s) => s.lat },
    { key: "lng", header: "경도", render: (s) => s.lng },
    { key: "areaCode", header: "지역코드", sortKey: "areaCode", render: (s) => s.areaCode },
    { key: "areaDetailCode", header: "지역상세코드", sortKey: "areaDetailCode", render: (s) => s.areaDetailCode },
    {
      key: "createdAt",
      header: "생성일자",
      sortKey: "createdAt",
      render: (s) => new Date(s.createdAt).toLocaleDateString(),
    },
    {
      key: "updatedAt",
      header: "수정일자",
      sortKey: "updatedAt",
      render: (s) =>
        s.updatedAt ? new Date(s.updatedAt).toLocaleString() : "기록 없음",
    },
  ];

  return (
    <div>
      <DataTable<Store, StoreSortKey>
        title="Stores Table"
        totalCount={totalCount}
        isLoading={isLoading}
        rows={rows}
        columns={columns}
        sortKey={sortKey}
        sortOrder={sortOrder}
        onSort={handleSort}
        rowKey={(s) => s.id}
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
