/**
 * File: features/admin-page/components/Map.tsx
 * Description:
 *   Leaflet 지도를 렌더링하고 사용자 위치를 표시하는 UI 컴포넌트
 */

import { useEffect, useRef, useState } from "react";
import StoreDetailPanel from "../../map-conponent/store-detail-panel/components/StoreDetailPanel";
import { useMapInit } from "../hooks/useMapInit";
import { useUserLocation } from "../hooks/useUserLocation";
import "leaflet/dist/leaflet.css";
import { useStoreData } from "../hooks/useStoreData";
import { useRenderKeyEvent } from "../hooks/useRenderKeyEvents";
import RecenterButton from "./RecenterButton";
import { useZoomScale } from "../hooks/useZoomScale";
import type { Store } from "../types/StoreItem";
import { usePreference } from "../hooks/usePreference";

function Map() {
    const mapRef = useRef<HTMLDivElement>(null);
    const leafletMap = useRef<L.Map | null>(null);
    const userMarkerRef = useRef<L.Layer | null>(null);
    const circleRef = useRef<L.Circle | null>(null);

    const [selectedStore, setSelectedStore] = useState<Store | null>(null);

    const storeMarkersRef = useRef<Record<string, L.Marker>>({});

    // 사용자별 가중치 로딩
    const { preference, refreshPreference } = usePreference();
    const [isWeight, setIsWeight] = useState(false);

    useEffect(() => {
        (async () => {
            await refreshPreference();
            setIsWeight(true);
        })();
    }, []);

    const distanceWeight = preference.distanceWeight;
    const priceWeight = preference.priceWeight;

    // 지역/거리 변경 시 강제 렌더링 트리거
    const renderKey = useRenderKeyEvent();

    // 지도 초기화
    useMapInit(mapRef, leafletMap);

    // 사용자 현재 위치 주기적으로 갱신
    useUserLocation(leafletMap, userMarkerRef);

    // 매장 마커 표시
    useStoreData({
        map: leafletMap.current,
        storeMarkersRef,
        circleRef,
        renderKey,
        distanceWeight,
        priceWeight,
        setSelectedStore,
        isWeight
    });

    // 줌 레벨에 따른 마커 크기 조절
    useZoomScale(leafletMap.current);

    return (
        <div style={{ position: "relative", width: "100vw", height: "100dvh" }}>
            {/* 지도 표시 영역 */}
            <div ref={mapRef} id="map" style={{ width: "100%", height: "100%" }} />

            <RecenterButton leafletMap={leafletMap} />
            {/* 매장 상세 정보 패널 */}
            {selectedStore && (
                <StoreDetailPanel
                    store={selectedStore}
                    onClose={() => setSelectedStore(null)} />
            )}
        </div>
    );
}

export default Map;