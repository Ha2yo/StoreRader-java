export interface Store {
    id: number;
    storeId: number;
    storeName: string;
    telNo: string | null;
    postNo: string | null;
    jibunAddr: string;
    roadAddr: string;
    lat: number | null;
    lng: number | null;
    price?: number | null;
    distance?: number | null;
    inspect_day?: string | null;
}

export interface Props {
    store: Store;
    candidates: Store[];
    goodId: number;
    onClose: () => void;
}