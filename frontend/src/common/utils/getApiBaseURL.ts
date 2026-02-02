export const getApiBaseURL = () => {
  const { hostname } = window.location;
  if (hostname === "localhost") {
    return import.meta.env.VITE_API_SERVER_URL;
  }
  return "/api";
};
