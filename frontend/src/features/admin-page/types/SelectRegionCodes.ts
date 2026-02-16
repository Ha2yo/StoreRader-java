export interface RegionCode {
    code: string;
    name: string;
    parentCode: string;
    level: number;
}

export type RegionCodeSortKey = "code" | "parentCode";
export type SortOrder = "asc" | "desc";