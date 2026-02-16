import useRegionCodesTable from "../../hooks/select/useRegionCodesTable";
import { DataTable, type Column } from "./DataTable";
import type { RegionCode, RegionCodeSortKey } from "../../types/SelectRegionCodes";
import Pagination from "./Pagination";

export default function SelectRegionCodes() {
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
  } = useRegionCodesTable();

     const columns: Column<RegionCode, RegionCodeSortKey>[] = [
    { key: "code", header: "지역 코드", sortKey: "code", render: (r) => r.code },
    { key: "name", header: "지역 이름", render: (r) => r.name },
    { key: "parentCode", header: "상위 코드", sortKey: "parentCode",render: (r) => r.parentCode },
    { key: "level", header: "레벨", render: (r) => r.level }
  ];

  return (
    <div>
      <DataTable<RegionCode, RegionCodeSortKey>
        title="Region Codes Table"
        totalCount={totalCount}
        isLoading={isLoading}
        rows={rows}
        columns={columns}
        sortKey={sortKey}
        sortOrder={sortOrder}
        onSort={handleSort}
        rowKey={(r) => r.code}
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
