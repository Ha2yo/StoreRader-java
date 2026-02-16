import type { Store, StoreSortKey } from "../../types/SelectStores";
import { useServerTable } from "./useServerTable";
import { selectStoresTable } from "../../apis/select/selectStoresTable";

export default function useStoresTable() {
 return useServerTable<Store, StoreSortKey>({
  fetcher: selectStoresTable,
  size: 10,
  initialSortKey: "id",
  initialSortOrder: "asc"
 });
}
