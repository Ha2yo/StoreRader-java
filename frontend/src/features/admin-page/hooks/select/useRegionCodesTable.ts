import type { RegionCode, RegionCodeSortKey } from "../../types/SelectRegionCodes";
import { useServerTable } from "./useServerTable";
import { selectRegionCodesTable } from "../../apis/select/selectRegionCodesTable";

export default function useRegionCodesTable() {
 return useServerTable<RegionCode, RegionCodeSortKey>({
  fetcher: selectRegionCodesTable,
  size: 10,
  initialSortKey: "code",
  initialSortOrder: "asc"
 });
}
