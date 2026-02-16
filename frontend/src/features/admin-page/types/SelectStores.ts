export interface Store {
    id: number;
    storeId: number;
    storeName: string;
    telNo: string;
    postNo: string;
    jibunAddr: string;
    roadAddr: string;
    lat: number;
    lng: number;
    areaCode: string;
    areaDetailCode: string;
    createdAt: string;
    updatedAt: string;
}

export type StoreSortKey = "id" | "storeId" | "createdAt" | "areaCode" | "areaDetailCode" | "updatedAt";
export type SortOrder = "asc" | "desc";