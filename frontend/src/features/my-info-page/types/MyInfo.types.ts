// 사용자 기본 정보 타입
export interface UserResponse {
  id: number;
  name: string;
  email: string;
  picture: string;
  role: "USER" | "ADMIN";
}

// 최종 로그인 응답 타입
export interface GoogleLoginResponse {
  user: UserResponse;
}

export interface UserHistoryItem {
    id: number;
    store_id: string;
    store_name: string;
    good_id: number;
    good_name: string;
    price: number;
    current_price: number;
    x_coord: number | null;
    y_coord: number | null;
    created_at: string;
}
