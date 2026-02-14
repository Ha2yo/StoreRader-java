import { useEffect, useState } from "react";
import type { Good } from "../types/GoodItem";
import { findAllGood } from "../apis/findAllGood";

export function useGoods() {
    const [goods, setGoods] = useState<Good[]>([]);

    useEffect(() => {
        let alive = true;

        (async () => {
            try {
                const data = await findAllGood();
                if (alive) setGoods(data);
            } catch (e) {
                console.error(e);
            }
        })();

        return () => {
            alive = false;
        };
    }, []);

    return goods;
}
