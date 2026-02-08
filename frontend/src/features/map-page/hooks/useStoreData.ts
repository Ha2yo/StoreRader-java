import { useEffect } from "react";
import L from "leaflet";
import { findAllStore } from "../apis/findAllStore";
import type { StoreDataProps } from "../types/StoreDataProps";
import { blackIcon } from "../utils/makerIcon";
import { loadSavedPosition } from "../../../common/utils/loadSavedPos";
import { calcDistance } from "../../../common/utils/calcDistance";

export function useStoreData({
    map,
    storeMarkersRef,
    circleRef,
    renderKey
}: StoreDataProps) {
    useEffect(() => {
        if (!map) return;

        (async () => {
            const stores = await findAllStore();

            const selectedRegion = localStorage.getItem("selectedRegionCode");
            const selectedDistance = localStorage.getItem("selectedDistance");

            const pos = loadSavedPosition();

            let filteredStores = stores;

            if (selectedDistance) {
                const maxDist = parseFloat(selectedDistance);

                filteredStores = stores.filter(
                    (s) => calcDistance(pos.lat, pos.lng, s.lat, s.lng) <= maxDist
                );

                if (circleRef.current) map.removeLayer(circleRef.current);
                circleRef.current = L.circle([pos.lat, pos.lng], {
                    radius: maxDist * 1000,
                    color: "#3388ff",
                    fillColor: "#3388ff",
                    fillOpacity: 0.15,
                    weight: 2,
                }).addTo(map);
                console.log(`${maxDist}km 이내 매장 수: ${filteredStores.length}`);
            }
            else if (selectedRegion !== "020000000") {
                // 전체 지역이 아닐 때만 지역 필터 사용
                if (circleRef.current) map.removeLayer(circleRef.current);
                filteredStores = stores.filter((s) => s.areaCode === selectedRegion);
                console.log(`지역 코드 ${selectedRegion} 매장 수: ${filteredStores.length}`);
            } else {
                if (circleRef.current) {
                    map.removeLayer(circleRef.current);
                    circleRef.current = null;
                }
            }
            Object.values(storeMarkersRef.current).forEach((m) => map.removeLayer(m));
            storeMarkersRef.current = {};

            filteredStores.forEach((store) => {

                const lat = store.lat;
                const lng = store.lng;

                const marker = L.marker([lat, lng], { icon: blackIcon }).addTo(map);
                storeMarkersRef.current[String(store.storeId)] = marker;

            });
        })();
    }, [map, renderKey]);
}