import apiClient from "../../../contexts/apiClient";

export async function requestGoogleLogout() {
    await apiClient.post("/auth/logout");
}