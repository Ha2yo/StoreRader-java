
import { useEffect, useState } from "react";
import type { RegionCode } from "../types/RegionCodeItem";
import { findAllRegionCodes } from "../apis/findAllRegionCodes";

const SOUTH_KOREA: RegionCode = {
    code: "020000000",
    name: "전체",
    parent_code: null,
    level: 0,
};

export function useRegionCodes() {
    const [regionCodes, setRegionCodes] = useState<RegionCode[]>([]);

    useEffect(() => {
        let alive = true;

        (async () => {
            try {
                const data = await findAllRegionCodes();
                console.log("regionCodes api:", data);

                const topRegions = data.filter((r) => r.level === 1);

                if (!alive) return;
                setRegionCodes([SOUTH_KOREA, ...topRegions]);
            } catch (e) {
                console.error("지역 코드 불러오기 실패:", e);
            }
        })();

        return () => {
            alive = false;
        };
    }, []);

    return regionCodes;
}