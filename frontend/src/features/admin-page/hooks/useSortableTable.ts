import { useEffect, useMemo, useState } from "react";

export type SortOrder = "asc" | "desc";

type Options<T, K extends string> = {
    fetcher: () => Promise<T[]>;
    initialSortKey: K;
    initialSortOrder?: SortOrder;
    getSortValue: (row: T, key: K) => number;
};

export function useSortableTable<T, K extends string>({
    fetcher,
    initialSortKey,
    initialSortOrder = "asc",
    getSortValue,
}: Options<T, K>) {
    const [rows, setRows] = useState<T[]>([]);
    const [isLoading, setIsLoading] = useState(true);
    const [sortKey, setSortKey] = useState<K>(initialSortKey);
    const [sortOrder, setSortOrder] = useState<SortOrder>(initialSortOrder);

    const sortedRows = useMemo(() => {
        return [...rows].sort((a, b) => {
            const aVal = getSortValue(a, sortKey);
            const bVal = getSortValue(b, sortKey);
            return sortOrder === "asc" ? aVal - bVal : bVal - aVal;
        });
    }, [rows, sortKey, sortOrder, getSortValue]);

    const handleSort = (key: K) => {
        if (sortKey === key) {
            setSortOrder((prev) => (prev === "asc" ? "desc" : "asc"));
            return;
        }
        setSortKey(key);
        setSortOrder("asc");
    };

    useEffect(() => {
        let mounted = true;

        const load = async () => {
            try {
                const data = await fetcher();
                if (mounted) setRows(data);
            } catch (err) {
                console.error("데이터 로딩 실패: ", err);
            } finally {
                if (mounted) setIsLoading(false);
            }
        };

        load();
        return () => {
            mounted = false;
        };
    }, [fetcher]);

    return { rows, sortedRows, isLoading, sortKey, sortOrder, handleSort, setRows };
}
