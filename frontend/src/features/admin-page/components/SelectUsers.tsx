/**
 * File: features/admin-page/components/SelectUsers.tsx
 * Description:
 *   관리자 페이지에서 유저 목록을 테이블 형태로 표시하는 UI 컴포넌트
 */

import useUsersTable from "../hooks/useUserTable";

function SelectUsers() {
    const { users, sortedUsers, isLoading, sortKey, sortOrder, handleSort } =
        useUsersTable();

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
                            {/* ID 정렬 */}
                            <th onClick={() => handleSort("id")} className="sortable">
                                ID {sortKey === "id" && (sortOrder === "asc" ? "▲" : "▼")}
                            </th>
                            <th>이름</th>
                            <th>이메일</th>
                            <th>권한</th>

                            {/* 가입일 정렬 */}
                            <th onClick={() => handleSort("createdAt")} className="sortable">
                                가입일 {sortKey === "createdAt" && (sortOrder === "asc" ? "▲" : "▼")}
                            </th>
                            
                            {/* 최근 로그인 정렬 */}
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