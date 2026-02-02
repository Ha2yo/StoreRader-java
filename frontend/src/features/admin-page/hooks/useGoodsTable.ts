import { useEffect, useState } from "react";
import type { Good } from "../types/selectGoods";
import { selectGoodsTable } from "../apis/select/selectGoodsTable";

type SortKey = "id" | "goodId" | "createdAt" | "updatedAt";
type SortOrder = "asc" | "desc";

function useGoodsTable() {
    const [goods, setGoods] = useState<Good[]>([]);
    const [isLoading, setIsLoading] = useState(true);
    const [sortKey, setSortKey] = useState<SortKey>("id");
    const [sortOrder, setSortOrder] = useState<SortOrder>("asc");

    // 정렬을 반영한 상품 리스트
    const sortedGoods = [...goods].sort((a, b) => {
        const getValue = (good: any) => {
            if (sortKey === "id") return good.id;
            if (sortKey === "goodId") return good.goodId;
            if (sortKey === "createdAt") return new Date(good.createdAt).getTime();
            if (sortKey === "updatedAt")
                return good.updatedAt ? new Date(good.updatedAt).getTime() : 0;
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

    // 최초 마운트 시 상품 리스트 조회
    useEffect(() => {
        const loadData = async () => {
            try {
                const data = await selectGoodsTable();
                setGoods(data);
            } catch (err) {
                console.error("데이터 로딩 실패: ", err);
            } finally {
                setIsLoading(false);
            }
        };
        loadData();
    }, []);

    return { goods, sortedGoods, isLoading, sortKey, sortOrder, handleSort };
}

export default useGoodsTable;