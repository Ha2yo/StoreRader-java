/**
 * File: features/my-info-page/api/requestGoogleLogout.tsx
 * Description:
 *   백엔드에 로그아웃 요청을 보내 세션을 종료한다
 */

import apiClient from "../../../contexts/apiClient";

export async function requestGoogleLogout() {
    await apiClient.post("/auth/logout");
}