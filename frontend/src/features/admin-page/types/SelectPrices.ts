export interface Price {
    id: number;
    goodId: number;
    storeId: number;
    inspectDay: string;
    price: number;
    isOnePlusOne: string;
    isDiscount: string;
    discountStart: string;
    discountEnd: string;
    createdAt: string;
}

export type PriceSortKey = "id" | "goodId" | "storeId" | "inspectDay" | "createdAt";
export type SortOrder = "asc" | "desc";