import { useEffect, useState } from "react";
import L from "leaflet";
import { findAllStore } from "../apis/findAllStore";
import type { StoreDataProps } from "../types/StoreDataProps";
import { blackIcon, orangeIcon, redIcon } from "../utils/makerIcon";
import { loadSavedPosition } from "../../../common/utils/loadSavedPos";
import { calcDistance } from "../../../common/utils/calcDistance";
import { findPrice } from "../apis/findPrices";
import type { StorePrice } from "../types/StorePriceItem";
import { calcEfficiency } from "../utils/calcEfficiency";
import type { Store } from "../types/StoreItem";

export function useStoreData({
    map,
    storeMarkersRef,
    circleRef,
    renderKey,
    setSelectedStore
}: StoreDataProps) {
    const [scoredStores, setScoredStores] = useState<Store[]>([]);

    useEffect(() => {
        if (!map) return;

        (async () => {
            const stores = await findAllStore();

            const selectedRegion = localStorage.getItem("selectedRegionCode");
            const selectedDistance = localStorage.getItem("selectedDistance");
            let selectedGoodName =
                localStorage.getItem("selectedGoodName") ||
                localStorage.getItem("lastSearchTerm");

            const pos = loadSavedPosition();

            let priceData: StorePrice[] = [];

            if (selectedGoodName)
                priceData = await findPrice(selectedGoodName);
             console.log("[priceData] len =", priceData.length, priceData.slice(0, 3));

            // 지역/거리 필터
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

            // 기존 마커 전체 제거
            Object.values(storeMarkersRef.current).forEach((m) => map.removeLayer(m));
            storeMarkersRef.current = {};

            // 추천 시스템
            if (selectedGoodName && priceData.length > 0) {
                const validStores = filteredStores.filter((s) =>
                    priceData.some((p) => p.storeId === s.storeId)
                );

                if (validStores.length == 0) return;

                const maxPrice = Math.max(...priceData.map((p) => p.price));
                const distances = validStores.map((s) =>
                    calcDistance(pos.lat, pos.lng, s.lat, s.lng)
                );
                const maxDistance = distances.length > 0 ? Math.max(...distances) : 1;

                // 유저 선호도 기반 가격/거리 효율 점수 계산
                const scored = validStores.map((store) => {
                    const matched = priceData.find((p) => p.storeId === store.storeId);
                    const price = matched?.price ?? maxPrice;
                    const distance = calcDistance(pos.lat, pos.lng, store.lat, store.lng);
                    const inspectDay = matched?.inspectDay;

                    const score = calcEfficiency(
                        price, distance, maxPrice, maxDistance, 0.5, 0.5
                    );

                    return { ...store, price, distance, inspectDay, score };
                });

                scored.sort((a, b) => b.score - a.score);
                setScoredStores(scored);

                // 최적 매장 자동 포커싱
                if (scored.length > 0) {
                    const top = scored[0];
                    map.flyTo([top.lat, top.lng], 16, {
                        animate: true,
                        duration: 1.5
                    });
                }

                scored.sort((a, b) => b.score - a.score);

                scored.forEach((store, idx) => {
                    let icon = blackIcon;
                    if (idx === 0) icon = redIcon;
                    else if (idx < 5) icon = orangeIcon;

                    const marker = L.marker([store.lat, store.lng], { icon }).addTo(map);

                    marker.bindTooltip(
                        `₩${store.price.toLocaleString()}`,
                        {
                            permanent: true,
                            direction: "top",
                            offset: L.point(0, -30),
                            className: "price-tooltip",
                        }
                    ).openTooltip();

                    if (idx === 0) {
                        marker.bindTooltip(`
                                <b>추천 매장 (${idx + 1}위)</b><br/>
                                ₩${store.price.toLocaleString()}<br/>
                                ${store.distance.toFixed(2)} km<br/>
                                효율 점수: ${store.score.toFixed(2)}`,
                            {
                                permanent: true,
                                direction: "top",
                                offset: L.point(0, -30),
                            }
                        ).openTooltip();

                    } else {
                        marker.bindPopup(`
                                <b>추천 매장 (${idx + 1}위)</b><br/>
                                ₩${store.price.toLocaleString()}<br/>
                                ${store.distance.toFixed(2)} km<br/>
                                효율 점수: ${store.score.toFixed(2)}`,
                            {
                                offset: L.point(0, -15),
                            }
                        );

                        storeMarkersRef.current[store.storeId] = marker;
                        marker.on("click", () => setSelectedStore(store));
                    }
                });
            }
            // 기본 모드
            else {
                filteredStores.forEach((store) => {

                    const marker = L.marker([store.lat, store.lng], { icon: blackIcon }).addTo(map);
                    marker.on("click", () => setSelectedStore(store));
                    storeMarkersRef.current[store.storeId] = marker;

                });
            }

        })();
    }, [map, renderKey]);
    return scoredStores;
}