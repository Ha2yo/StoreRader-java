/**
 * File: features/admin-page/hooks/useUserLocation.tsx
 * Description:
 *   저장된 사용자 위치 정보를 기반으로
 *   지도 위 사용자 위치 마커를 주기적으로 갱신한다
 */

import { useEffect } from "react";
import L from "leaflet";
import { loadSavedPosition } from "../../../common/utils/loadSavedPos";
import { userIcon } from "../utils/userIcon";

export function useUserLocation(
    leafletMap: React.RefObject<L.Map | null>,
    markerRef: React.RefObject<L.Layer | null>
) {
    useEffect(() => {
        const map = leafletMap.current!;
        if (!map) return;

        const refreshMarker = () => {
            const pos = loadSavedPosition();
            if (!pos) return;

            // 중복이 생기지 않게 기존 마커 제거
            if (markerRef.current) map.removeLayer(markerRef.current);

            markerRef.current = L.marker([pos.lat, pos.lng], {
                icon: userIcon,
                zIndexOffset: 9999,
            }).addTo(map);
        };

        refreshMarker();
        const id = setInterval(refreshMarker, 5000);

        return () => clearInterval(id);
    }, []);
}