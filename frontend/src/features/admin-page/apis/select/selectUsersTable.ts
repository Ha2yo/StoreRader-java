/**
 * File: features/admin-page/api/fetchUsersTable.tsx
 * Description:
 *   서버로부터 사용자 목록을 조회한다
 */

import apiClient from "../../../../contexts/apiClient";

export async function selectUsersTable() {
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