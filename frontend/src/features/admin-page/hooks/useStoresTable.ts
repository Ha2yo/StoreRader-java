import { useEffect, useState } from "react";
import type { Store } from "../types/selectStores";
import { selectStoresTable } from "../apis/select/selectStoresTable";

type SortKey = "id" | "storeId" | "createdAt" | "updatedAt";
type SortOrder = "asc" | "desc";

function useStoresTable() {
    const [stores, setStores] = useState<Store[]>([]);
    const [isLoading, setIsLoading] = useState(true);
    const [sortKey, setSortKey] = useState<SortKey>("id");
    const [sortOrder, setSortOrder] = useState<SortOrder>("asc");

    // 정렬을 반영한 상품 리스트
    const sortedStores = [...stores].sort((a, b) => {
        const getValue = (store: any) => {
            if (sortKey === "id") return store.id;
            if (sortKey === "storeId") return store.storeId;
            if (sortKey === "createdAt") return new Date(store.createdAt).getTime();
            if (sortKey === "updatedAt")
                return store.updatedAt ? new Date(store.updatedAt).getTime() : 0;
        };

        const aVal = getValue(a);
        const bVal = getValue(b);

        return sortOrder === "asc" ? aVal - bVal : bVal - aVal;
    });

    // 정렬 기준 변경 처리
    const handleSort = (key: SortKey) => {
        if (sortKey === key) {
            setSortOrder((prev) => (prev === "asc" ? "desc" : "asc"));
        } else {
            setSortKey(key);
            setSortOrder("asc");
        }
    };

    // 최초 마운트 시 매장 리스트 조회
    useEffect(() => {
        const loadData = async () => {
            try {
                const data = await selectStoresTable();
                setStores(data);
            } catch (err) {
                console.error("데이터 로딩 실패: ", err);
            } finally {
                setIsLoading(false);
            }
        };
        loadData();
    }, []);

    return { stores, sortedStores, isLoading, sortKey, sortOrder, handleSort };
}

export default useStoresTable;