/**
 * File: features/admin-page/hooks/useUserTable.tsx
 * Description:
 *   관리자 페이지에서 유저 목록을 조회하고 정렬 상태를 관리한다
 */

import { useEffect, useState } from "react";
import type { User } from "../types/SelectUsers";
import { selectUsersTable } from "../apis/select/selectUsersTable";

type SortKey = "id" | "createdAt" | "lastLogin";
type SortOrder = "asc" | "desc";

function useUsersTable() {
    const [users, SetUsers] = useState<User[]>([]);
    const [isLoading, setIsLoading] = useState(true);
    const [sortKey, setSortKey] = useState<SortKey>("id");
    const [sortOrder, setSortOrder] = useState<SortOrder>("asc");

    // 정렬을 반영한 유저 리스트
    const sortedUsers = [...users].sort((a, b) => {
        const getValue = (user: any) => {
            if (sortKey === "id") return user.id;
            if (sortKey === "createdAt") return new Date(user.createdAt).getTime();
            if (sortKey === "lastLogin")
                return user.lastLogin ? new Date(user.lastLogin).getTime() : 0;
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

    // 최초 마운트 시 유저 리스트 조회
    useEffect(() => {
        const loadData = async () => {
            try {
                const data = await selectUsersTable();
                SetUsers(data);
            } catch (err) {
                console.error("데이터 로딩 실패: ", err);
            } finally {
                setIsLoading(false);
            }
        };
        loadData();
    }, []);

    return { users, sortedUsers, isLoading, sortKey, sortOrder, handleSort };
}

export default useUsersTable;