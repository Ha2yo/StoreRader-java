import axios from "axios";

const getBaseURL = () => {
  const { hostname } = window.location;

  if(hostname === "localhost") {
    return import.meta.env.VITE_API_SERVER_URL;
  }
  return "/api";
}

const apiClient = axios.create({
  baseURL: getBaseURL(),
  withCredentials: true,
});

let refreshPromise: Promise<void> | null = null;
apiClient.interceptors.response.use(
  (response) => response,
  async (error) => {
    const originalRequest = error.config;

    if (!error.response) return Promise.reject(error);

    if (originalRequest?.url?.includes("/auth/refresh")) {
      return Promise.reject(error);
    }

    if (error.response.status === 401 && !originalRequest._retry) {
      originalRequest._retry = true;

      try {
        console.log("Access token 만료. 갱신 시도 중...");

        if (!refreshPromise) {
          refreshPromise = apiClient
            .post("/auth/refresh")
            .then(() => {})
            .finally(() => {
              refreshPromise = null;
            });
        }

        await refreshPromise;

        console.log("토큰 갱신 성공");
        return apiClient(originalRequest);
      } catch (err) {
        console.log("세션 만료");
        return Promise.reject(err);
      }
    }

    return Promise.reject(error);
  }
);

export default apiClient;
