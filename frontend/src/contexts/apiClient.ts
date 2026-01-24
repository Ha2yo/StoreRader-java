import axios from "axios";

const apiClient = axios.create({
    baseURL: "/api",
    withCredentials: true,
});

apiClient.interceptors.response.use(
    (response) => response,
    async (error) => {
        const originalRequest = error.config;

        if(error.response?.status === 401 && !originalRequest._retry) {
            originalRequest._retry = true;

            try {
                console.log("Access token 만료. 갱신 시도 중...");

                await axios.post("/api/auth/refresh", {}, { withCredentials:true });

                console.log("토큰 갱신 성공");

                return apiClient(originalRequest);
            } catch(error) {
                console.log("세션 만료")
            }
        }
    }
);

export default apiClient;