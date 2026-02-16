export interface User {
  id: number;
  role: string;
  name: string;
  email: string;
  sub: string;
  picture: string;
  refreshToken: string;
  refreshTokenExpiresAt: string;
  createdAt: string;
  lastLogin: string;
}

export type UsersSortKey = "id" | "role" | "createdAt" | "lastLogin";
export type SortOrder = "asc" | "desc";