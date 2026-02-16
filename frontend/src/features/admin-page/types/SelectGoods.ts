export interface Good {
    id: number;
    goodId: number;
    goodName: string;
    totalCnt: number;
    totalDivCode: string;
    createdAt: string;
    updatedAt: string | null;
}

export type GoodsSortKey = "id" | "goodId" | "totalDivCode" | "createdAt" | "updatedAt";
export type SortOrder = "asc" | "desc";