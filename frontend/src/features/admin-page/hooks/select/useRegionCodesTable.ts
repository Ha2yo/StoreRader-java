import type { RegionCode } from "../../types/SelectRegionCodes";
import { selectRegionCodesTable } from "../../apis/select/selectRegionCodesTable";
import { useSortableTable } from "./useServerTable.ts";

type SortKey = "parentCode" | "level";

export default function useRegionCodesTable() {
  const table = useSortableTable<RegionCode, SortKey>({
    fetcher: selectRegionCodesTable,
    initialSortKey: "level",
    getSortValue: (r, key) => {
      switch (key) {
        case "parentCode":
          return Number(r.parentCode);
        case "level":
          return r.level;
      }
    },
  });

  return {
    regionCodes: table.rows,
    sortedRegionCodes: table.sortedRows,
    isLoading: table.isLoading,
    sortKey: table.sortKey,
    sortOrder: table.sortOrder,
    handleSort: table.handleSort,
    setRegionCodes: table.setRows,
  };
}
