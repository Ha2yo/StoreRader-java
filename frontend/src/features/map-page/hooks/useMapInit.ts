import { useEffect } from "react";
import L from "leaflet";
import { loadSavedPosition } from "../../../common/utils/loadSavedPos";

export function useMapInit(
    mapRef: React.RefObject<HTMLDivElement | null>,
    leafletMap: React.RefObject<L.Map | null>
) {
    useEffect(() => {
        // 중복 생성 방지
        if (!mapRef.current || leafletMap.current) return;

        const map = L.map(mapRef.current, { zoomControl: false, attributionControl: false, });
        leafletMap.current = map;

        L.tileLayer("https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png", {
            attribution:
                '&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors',
        }).addTo(map);

        const pos = loadSavedPosition();
        if (pos) map.setView([pos.lat, pos.lng], 16);
    }, []);
}