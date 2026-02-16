import { useEffect, useState } from "react";
import type { SortOrder } from "../../types/SelectGoods";
import type { PageResult } from "../../types/PageResult";

export function useServerTable<T, K extends string>(opts: {
    fetcher: (p: { page: number; size: number; sortKey: K; sortOrder: SortOrder }) => Promise<PageResult<T>>;
    initialPage?: number;
    size?: number;
    initialSortKey: K;
    initialSortOrder?: SortOrder;
}) {
    const size = opts.size ?? 10;

    const [page, setPage] = useState(opts.initialPage ?? 0);
    const [sortKey, setSortKey] = useState<K>(opts.initialSortKey);
    const [sortOrder, setSortOrder] = useState<SortOrder>(opts.initialSortOrder ?? "asc");

    const [rows, setRows] = useState<T[]>([]);
    const [totalCount, setTotalCount] = useState(0);
    const [isLoading, setIsLoading] = useState(true);

    const handleSort = (key: K) => {
        if (key === sortKey) setSortOrder((o) => (o === "asc" ? "desc" : "asc"));
        else {
            setSortKey(key);
            setSortOrder("asc");
        }
        setPage(0);
    };

    useEffect(() => {
        let mounted = true;
        setIsLoading(true);

        opts.fetcher({ page, size, sortKey, sortOrder })
            .then((data) => {
                if (!mounted) return;
                setRows(data.items);
                setTotalCount(data.totalCount);
            })
            .catch((e) => console.error("table fetch failed:", e))
            .finally(() => {
                if (mounted) setIsLoading(false);
            });

        return () => {
            mounted = false;
        };
    }, [page, size, sortKey, sortOrder, opts.fetcher]);

    const canPrev = page > 0;
    const canNext = (page + 1) * size < totalCount;

    return {
        rows,
        totalCount,
        isLoading,
        page,
        size,
        canPrev,
        canNext,
        setPage,
        sortKey,
        sortOrder,
        handleSort,
    };
}
