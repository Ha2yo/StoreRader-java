import type { Price } from "../../types/SelectPrices";
import { selectPricesTable } from "../../apis/select/selectPricesTable";
import { useSortableTable } from "./useServerTable.ts";

type SortKey = "id" | "goodId" | "storeId" | "inspectDay" | "createdAt";

export default function usePricesTable() {
    const table = useSortableTable<Price, SortKey>({
        fetcher: selectPricesTable,
        initialSortKey: "id",
        getSortValue: (p, key) => {
            switch (key) {
                case "id":
                    return p.id;
                case "goodId":
                    return p.goodId;
                case "storeId":
                    return p.storeId;
                case "inspectDay":
                    return Number([p.inspectDay]);
                case "createdAt":
                    return new Date(p.createdAt).getTime();
            }
        },
    });

    return {
        prices: table.rows,
        sortedPrices: table.sortedRows,
        isLoading: table.isLoading,
        sortKey: table.sortKey,
        sortOrder: table.sortOrder,
        handleSort: table.handleSort,
        setPrices: table.setRows,
    };
}
