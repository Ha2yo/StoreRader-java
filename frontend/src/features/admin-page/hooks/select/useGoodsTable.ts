import type { Good } from "../../types/SelectGoods";
import { selectGoodsTable } from "../../apis/select/selectGoodsTable";
import { useSortableTable } from "../useSortableTable";

type SortKey = "id" | "goodId" | "createdAt" | "updatedAt";

export default function useGoodsTable() {
  const table = useSortableTable<Good, SortKey>({
    fetcher: selectGoodsTable,
    initialSortKey: "id",
    getSortValue: (g, key) => {
      switch (key) {
        case "id":
          return g.id;
        case "goodId":
          return g.goodId;
        case "createdAt":
          return new Date(g.createdAt).getTime();
        case "updatedAt":
          return g.updatedAt ? new Date(g.updatedAt).getTime() : 0;
      }
    },
  });

  return {
    goods: table.rows,
    sortedGoods: table.sortedRows,
    isLoading: table.isLoading,
    sortKey: table.sortKey,
    sortOrder: table.sortOrder,
    handleSort: table.handleSort,
    setGoods: table.setRows,
  };
}
