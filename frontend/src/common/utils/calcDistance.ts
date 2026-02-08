export function calcDistance(
    slat: number, slng: number, dlat: number, dlng: number) {
    const radius = 6371; // 지구 반경 (km)
    const toRadian = Math.PI / 180;

    const deltaLat = Math.abs(slat - dlat) * toRadian;
    const deltaLng = Math.abs(slng - dlng) * toRadian;

    const sinDeltaLat = Math.sin(deltaLat / 2);
    const sinDeltaLng = Math.sin(deltaLng / 2);
    const squareRoot = Math.sqrt(
        sinDeltaLat * sinDeltaLat +
        Math.cos(slat * toRadian) * Math.cos(dlat * toRadian) * sinDeltaLng * sinDeltaLng);

    const distance = 2 * radius * Math.asin(squareRoot);

    return distance;
}