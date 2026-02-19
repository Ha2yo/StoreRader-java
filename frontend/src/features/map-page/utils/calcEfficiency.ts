export function calcEfficiency(
    price: number,
    distance: number,
    maxPrice: number,
    maxDistance: number,
    distanceWeight: number,
    priceWeight: number
): number {
    // 각 항목을 0~1 사이로 정규화
    const priceRatio = price / maxPrice;
    const distanceRatio = distance / maxDistance;

    // 값이 낮을수록 효율이 높으므로 반전 (1-ratio)
    const priceEff = 1 - priceRatio;
    const distanceEff = 1 - distanceRatio;

    // 사용자 선호도(가중치)에 따라 점수 계산
    const efficiency = priceWeight * priceEff + distanceWeight * distanceEff;

    // 점수 범위를 0~100으로 확장
    return efficiency * 100;
}