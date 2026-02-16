import type { Store } from "../../types/SelectStores";
import { selectStoresTable } from "../../apis/select/selectStoresTable";
import { useSortableTable } from "./useSortableTable";

type SortKey = "id" | "storeId" | "createdAt" | "updatedAt";

export default function useStoresTable() {
    const table = useSortableTable<Store, SortKey>({
        fetcher: selectStoresTable,
        initialSortKey: "id",
        getSortValue: (s, key) => {
            switch (key) {
                case "id":
                    return s.id;
                case "storeId":
                    return s.storeId;
                case "createdAt":
                    return new Date(s.createdAt).getTime();
                case "updatedAt":
                    return s.updatedAt ? new Date(s.updatedAt).getTime() : 0;
            }
        },
    });

    return {
        stores: table.rows,
        sortedStores: table.sortedRows,
        isLoading: table.isLoading,
        sortKey: table.sortKey,
        sortOrder: table.sortOrder,
        handleSort: table.handleSort,
        setUsers: table.setRows,
    };
}