import apiClient from "../../../contexts/apiClient";
import type { User } from "../types/fetchUsers";

export async function fetchUsersTable(): Promise<User[]>{
  try {
    const res = await apiClient.get(
      "/admin/select/users"
    );

    return res.data.users;
  } catch (error) {
    console.error("유저 테이블 조회 실패: ", error);
    throw error;
  }
}