import { useEffect, useState } from "react";
import type { User } from "../types/fetchUsers";
import { fetchUsersTable } from "../apis/fetchUsersTable";

function UserTable() {
    const [users, SetUsers] = useState<User[]>([]);
    const [isLoading, setIsLoading] = useState(true);

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
            <div>
                <h1>사용자 관리 (Users Table)</h1>
                <span>총 {users.length}명</span>
            </div>

            <div>
                <table>
                    <thead>
                        <tr>
                            <th>ID</th>
                            <th>이름</th>
                            <th>이메일</th>
                            <th>권한</th>
                            <th>가입일</th>
                            <th>최근 로그인</th>
                        </tr>
                    </thead>
                    <tbody>
                        {users.map((user) => (
                            <tr key={user.id}>
                                <td>{user.id}</td>
                                <td>{user.name}</td>
                                <td>{user.email}</td>
                                <td>
                                    <span>
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

export default UserTable;