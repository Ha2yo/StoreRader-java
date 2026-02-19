import { useState } from "react";
import { findUserPreference } from "../apis/findUserPreference";
import type { UserPreference } from "../types/UserPreference";

export function usePreference() {
    const [preference, setPreference] = useState<UserPreference>({
        distanceWeight: 0.5,
        priceWeight: 0.5,
    });

    // 서버에서 최신 가중치 로드
    const refreshPreference = async () => {
        const data = await findUserPreference();
        setPreference(data);
        console.log("[선호도 로드 완료]", JSON.stringify(data, null, 2));
    };

    return {
        preference,
        setPreference,
        refreshPreference,
    };
}