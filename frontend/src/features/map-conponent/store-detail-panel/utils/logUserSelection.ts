import { insertUserSelection } from "../apis/insertUserSelection";
import type { Store } from "../types/StoreDetail";

export async function logUserSelection (
    store: Store,
    goodId: number,
    preferenceType: string
) {

    const payload = {
        storeId: store.storeId,
        goodId: goodId,
        price: store.price,
        preferenceType: preferenceType,

    };
    console.log("서버 전송 데이터:", JSON.stringify(payload, null, 2));

    await insertUserSelection(payload);


}