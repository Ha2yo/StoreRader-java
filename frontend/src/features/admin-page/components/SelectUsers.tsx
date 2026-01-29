import { useEffect, useState } from "react";
import type { User } from "../types/fetchUsers";
import { fetchUsersTable } from "../apis/fetchUsersTable";

function SelectUsers() {
    const [users, SetUsers] = useState<User[]>([]);
    const [isLoading, setIsLoading] = useState(true);

    type SortKey = "id" | "createdAt" | "lastLogin";
    type SortOrder = "asc" | "desc";

    const [sortKey, setSortKey] = useState<SortKey>("id");
    const [sortOrder, setSortOrder] = useState<SortOrder>("asc");

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

    const handleSort = (key: SortKey) => {
        if (sortKey === key) {
            setSortOrder((prev) => (prev === "asc" ? "desc" : "asc"));
        } else {
            setSortKey(key);
            setSortOrder("asc");
        }
    };

    useEffect(() => {
        const loadData = async () => {
            try {
                const data = await fetchUsersTable();
                SetUsers(data);
            } catch (err) {
                console.error("데이터 로딩 실패: ", err);
            } finally {
                setIsLoading(false);
            }
        };
        loadData();
    }, []);

    if (isLoading)
        return <div>데이터를 불러오는 중입니다...</div>

    return (
        <div className="container">
            <div className="headerRow">
                <h1>Users Table</h1>
                <span>총 {users.length}명</span>
            </div>

            <div className="tableWrap">
                <table className="table">
                    <thead>
                        <tr>
                            <th onClick={() => handleSort("id")} className="sortable">
                                ID {sortKey === "id" && (sortOrder === "asc" ? "▲" : "▼")}
                            </th>
                            <th>이름</th>
                            <th>이메일</th>
                            <th>권한</th>
                            <th onClick={() => handleSort("createdAt")} className="sortable">
                                가입일 {sortKey === "createdAt" && (sortOrder === "asc" ? "▲" : "▼")}
                            </th>
                            <th onClick={() => handleSort("lastLogin")} className="sortable">
                                최근 로그인 {sortKey === "lastLogin" && (sortOrder === "asc" ? "▲" : "▼")}
                            </th>
                        </tr>
                    </thead>
                    <tbody>
                        {sortedUsers.map((user) => (
                            <tr key={user.id}>
                                <td>{user.id}</td>
                                <td>{user.name}</td>
                                <td>{user.email}</td>
                                <td>
                                    <span >
                                        {user.role}
                                    </span>
                                </td>
                                <td>
                                    {new Date(user.createdAt).toLocaleDateString()}
                                </td>
                                <td>
                                    {user.lastLogin ? new Date(user.lastLogin).toLocaleString() : "기록 없음"}
                                </td>
                            </tr>
                        ))}
                    </tbody>
                </table>
            </div>
        </div>
    );
}

export default SelectUsers;