import type { Price, PriceSortKey } from "../../types/SelectPrices";
import { useServerTable } from "./useServerTable";
import { selectPricesTable } from "../../apis/select/selectPricesTable";

export default function useRegionCodesTable() {
 return useServerTable<Price, PriceSortKey>({
  fetcher: selectPricesTable,
  size: 10,
  initialSortKey: "id",
  initialSortOrder: "asc"
 });
}
