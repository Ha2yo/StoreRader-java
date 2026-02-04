import useRegionCodesTable from "../../hooks/select/useRegionCodesTable";
import { DataTable, type Column } from "./DataTable";
import type { RegionCode } from "../../types/SelectRegionCodes";

type SortKey = "parentCode" | "level";

export default function SelectRegionCodes() {
  const { regionCodes, sortedRegionCodes, isLoading, sortKey, sortOrder, handleSort } =
    useRegionCodesTable();

  const columns: Column<RegionCode, SortKey>[] = [
    { key: "code", header: "지역 코드", render: (r) => r.code },
    { key: "name", header: "지역 이름", render: (r) => r.name },
    { key: "parentCode", header: "상위 코드", sortKey: "parentCode",render: (r) => r.parentCode },
    { key: "level", header: "레벨", sortKey: "level", render: (r) => r.level }
  ];

  return (
    <DataTable
      title="RegionCodes Table"
      totalCount={regionCodes?.length ?? 0}
      isLoading={isLoading}
      rows={sortedRegionCodes}
      columns={columns}
      sortKey={sortKey}
      sortOrder={sortOrder}
      onSort={handleSort}
      rowKey={(r) => r.code}
    />
  );
}
