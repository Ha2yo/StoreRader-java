/**
 * File: features/admin-page/components/Map.tsx
 * Description:
 *   Leaflet 지도를 렌더링하고 사용자 위치를 표시하는 UI 컴포넌트
 */

import { useRef } from "react";
import { useMapInit } from "../hooks/useMapInit";
import { useUserLocation } from "../hooks/useUserLocation";
import { recenterMap } from "../utils/recenterMap";
import "leaflet/dist/leaflet.css";
import { useStoreData } from "../hooks/useStoreData";
import { useRenderKeyEvent } from "../hooks/useRenderKeyEvents";

function Map() {
    const mapRef = useRef<HTMLDivElement>(null);
    const leafletMap = useRef<L.Map | null>(null);
    const userMarkerRef = useRef<L.Layer | null>(null);

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
        renderKey,
    });

    return (
        <div style={{ position: "relative", width: "100vw", height: "100vh" }}>
            {/* 지도 표시 영역 */}
            <div ref={mapRef} id="map" style={{ width: "100%", height: "100%" }} />

            {/* 현재 위치로 이동 버튼 */}
            <button
                onClick={recenterMap(leafletMap)}
                style={{
                    position: "absolute",
                    bottom: "160px",
                    right: "80px",
                    zIndex: 1000,
                    backgroundColor: "#fff",
                    border: "none",
                    borderRadius: "50%",
                    width: "50px",
                    height: "50px",
                    display: "flex",
                    alignItems: "center",
                    justifyContent: "center",
                    cursor: "pointer",
                    boxShadow: "0 2px 6px rgba(0,0,0,0.3)",
                }}
                title="내 위치로 이동"
            >
                <svg
                    width="34"
                    height="34"
                    viewBox="0 0 24 24"
                    fill="none"
                    stroke="#007AFF"
                    strokeWidth="1"
                    strokeLinecap="round"
                    strokeLinejoin="round"
                    style={{ transform: "scale(2.8)" }}
                >
                    <path d="M12 3v2M12 19v2M5 12H3M21 12h-2" />
                    <circle cx="12" cy="12" r="8" />
                    <circle cx="12" cy="12" r="3" fill="#007AFF" />

                    <line x1="12" y1="2" x2="12" y2="5" />
                    <line x1="12" y1="19" x2="12" y2="22" />
                    <line x1="2" y1="12" x2="5" y2="12" />
                    <line x1="19" y1="12" x2="22" y2="12" />
                </svg>
            </button>


        </div>
    );
}

export default Map;