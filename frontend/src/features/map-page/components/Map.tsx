/**
 * File: features/admin-page/components/Map.tsx
 * Description:
 *   Leaflet 지도를 렌더링하고 사용자 위치를 표시하는 UI 컴포넌트
 */

import { useRef } from "react";
import { useMapInit } from "../hooks/useMapInit";
import { useUserLocation } from "../hooks/useUserLocation";
import "leaflet/dist/leaflet.css";
import { useStoreData } from "../hooks/useStoreData";
import { useRenderKeyEvent } from "../hooks/useRenderKeyEvents";
import RecenterButton from "./RecenterButton";

function Map() {
    const mapRef = useRef<HTMLDivElement>(null);
    const leafletMap = useRef<L.Map | null>(null);
    const userMarkerRef = useRef<L.Layer | null>(null);
    const circleRef = useRef<L.Circle | null>(null); 

    const storeMarkersRef = useRef<Record<string, L.Marker>>({});

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
    });

    return (
        <div style={{ position: "relative", width: "100vw", height: "100vh" }}>
            {/* 지도 표시 영역 */}
            <div ref={mapRef} id="map" style={{ width: "100%", height: "100%" }} />
        
            <RecenterButton leafletMap={leafletMap} />
        </div>
    );
}

export default Map;