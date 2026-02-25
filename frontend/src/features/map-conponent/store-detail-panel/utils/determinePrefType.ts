import type { Store } from "../types/StoreDetail";

export function determinePreferenceType(
    selected: Store,
    candidates: Store[]
    ) {
    const THRESHOLD  = 0.05;

    // 판단이 불가한 경우
    if (!candidates || candidates.length === 0) return "neutral";
    if (!selected.price || !selected.distance) return "neutral";

    // 비교 대상 그룹(candidates)의 평균 가격과 평균 거리 계싼
    const avgPrice = candidates.reduce((sum, s) => sum + (s.price ?? 0), 0) / candidates.length;
    const avgDist = candidates.reduce((sum, s) => sum + (s.distance ?? 0), 0) / candidates.length;

    // 평균 대비 절감 비율 계산
    const priceFocus = (avgPrice - selected.price) / avgPrice;      // 양수: 평균보다 저렴
    const distanceFocus = (avgDist - selected.distance) / avgDist;  // 양수: 평균보다 가까움

    // 사전에 설정한 임계값(THRESHOLD)을 이용하여 비교
    const priceLike = priceFocus > THRESHOLD;
    const distLike = distanceFocus > THRESHOLD;

    // 선호도 판정
    if (priceLike && !distLike) return "price";
    if (!priceLike && distLike) return "distance";

    // 기타 케이스 (둘 다 만족 or 둘 다 미묘할 때)
    return "neutral";
}
