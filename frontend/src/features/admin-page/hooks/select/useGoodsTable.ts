import type { Good, GoodsSortKey } from "../../types/SelectGoods";
import { useServerTable } from "./useServerTable";
import { selectGoodsTable } from "../../apis/select/selectGoodsTable";

export default function useGoodsTable() {
 return useServerTable<Good, GoodsSortKey>({
  fetcher: selectGoodsTable,
  size: 10,
  initialSortKey: "id",
  initialSortOrder: "desc"
 });
}
