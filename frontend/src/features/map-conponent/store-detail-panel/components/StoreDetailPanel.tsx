import { useState } from "react";
import type { Props } from "../types/StoreDetail";
import { calcDistance } from "../../../../common/utils/calcDistance";
import { loadSavedPosition } from "../../../../common/utils/loadSavedPos";
import { touchEffect } from "../../../../common/utils/touchEffect";

function StoreDetailPanel({ store, onClose }: Props) {
    const [isRouteModalOpen, setIsRouteModalOpen] = useState(false);

    // 사용자 위치 및 매장까지의 거리 계산
    const pos = loadSavedPosition();
    const distanceKm = calcDistance(pos.lat, pos.lng, store.lat!, store.lng!).toFixed(2)

    return (
        <>
            {/* 상세 패널 */}
            <div
                style={{
                    position: "absolute",
                    bottom: "0",

                    left: "50%",
                    transform: "translateX(-50%)",

                    width: "calc(100% - 32px)",
                    maxWidth: "1000px",

                    background: "#fff",
                    borderRadius: "20px",
                    boxShadow: "0 -4px 12px rgba(0,0,0,0.15)",
                    padding: "20px",
                    paddingBottom: "calc(env(safe-area-inset-bottom) + 24px)",
                    zIndex: 2000
                }}

            >
                {/* 매장 기본 정보 */}
                <h3 style={{ fontSize: "20px", fontWeight: "bold", marginBottom: "10px" }}>{store.storeName}</h3>
                <p style={{ fontSize: "15px", color: "#777", marginBottom: "4px" }}>{store.roadAddr}</p>
                <p style={{ fontSize: "14px", color: "#777", marginBottom: "14px" }}>{store.jibunAddr}</p>
                <p style={{
                    display: "flex",
                    alignItems: "center",
                    fontSize: "14px",
                    color: "#555",
                    marginBottom: "10px"
                }}>📞 {store.telNo ?? "전화번호 없음"}</p>

                {distanceKm && (
                    <p style={{
                        display: "flex",
                        alignItems: "center",
                        fontSize: "14px",
                        color: "#555",
                        marginBottom: "22px"
                    }}>{distanceKm} km</p>
                )}

                {/* 길찾기 & 닫기 */}
                <div style={{ display: "flex", gap: "12px", marginTop: "10px" }}>
                    <button
                        {...touchEffect}
                        style={{
                            flex: 1,
                            background: "#3182F6",
                            color: "#fff",
                            textShadow: "0 1px 2px rgba(0,0,0,0.2)",
                            padding: "12px 0",
                            borderRadius: "12px",
                            border: "none",
                            fontSize: "16px",
                            fontWeight: "bold"
                        }}
                        onClick={() => setIsRouteModalOpen(true)}
                    >
                        길찾기
                    </button>

                    <button
                        {...touchEffect}
                        style={{
                            flex: 1,
                            background: "#F3F4F6",
                            color: "#555",
                            textShadow: "0 1px 1px rgba(0,0,0,0.05)",
                            padding: "12px 0",
                            borderRadius: "12px",
                            border: "none",
                            fontSize: "16px",
                            fontWeight: "bold"
                        }}
                        onClick={onClose}
                    >
                        닫기
                    </button>
                </div>

            </div>

            {/* 길찾기 선택 모달 */}
            {isRouteModalOpen && (
                <div
                    style={{
                        position: "fixed",
                        top: 0,
                        left: 0,
                        width: "100vw",
                        height: "100dvh",
                        backgroundColor: "rgba(0, 0, 0, 0.45)",
                        backdropFilter: "blur(1px)",
                        display: "flex",
                        alignItems: "center",
                        justifyContent: "center",
                        zIndex: 3000,
                    }}
                    onClick={() => setIsRouteModalOpen(false)}
                >
                    <div
                        onClick={(e) => e.stopPropagation()}
                        style={{
                            background: "#fff",
                            borderRadius: "20px",
                            padding: "24px 20px",
                            width: "85%",
                            maxWidth: "380px",
                            textAlign: "center",
                            boxShadow: "0 8px 28px rgba(0,0,0,0.18)",
                        }}
                    >
                        <h3
                            style={{
                                margin: 0,
                                marginBottom: "20px",
                                fontSize: "18px",
                                fontWeight: 600
                            }}>
                            길찾기 앱 선택</h3>

                        {/* 네이버 지도 / 카카오맵 */}
                        <div
                            style={{
                                display: "flex",
                                justifyContent: "space-between",
                                gap: "14px",
                            }}
                        >
                            {/* 네이버 지도 */}
                            <button
                                {...touchEffect}
                                style={{
                                    flex: 1,
                                    aspectRatio: "1",
                                    borderRadius: "14px",
                                    background: "#fff",
                                    border: "none",
                                    boxShadow: "none",
                                    color: "#fff",
                                    fontSize: "15px",
                                    fontWeight: 600,
                                    display: "flex",
                                    justifyContent: "center",
                                    alignItems: "center",
                                }}
                                onClick={async () => {
                                    if (store.lat && store.lng) {
                                        const slat = pos.lat;
                                        const slng = pos.lng;
                                        const sname = encodeURIComponent("내 위치");
                                        const dlat = store.lat;
                                        const dlng = store.lng;
                                        const dname = encodeURIComponent(store.storeName);
                                        const naverMapAppUrl =
                                            `nmap://route/public?slat=${slat}&slng=${slng}&sname=${sname}&dlat=${dlat}&dlng=${dlng}&dname=${dname}&appname=com.ik9014.storerader`
                                        const naverMapWebUrl =
                                            `https://map.naver.com/p/directions/${slng},${slat},${sname}/${dlng},${dlat},${dname}/-/transit?c=15.00,0,0,0,dh`;

                                        const isMobile = /Android|iPhone|iPad|iPod/i.test(navigator.userAgent);

                                        if (isMobile) {
                                            window.location.href = naverMapAppUrl;

                                            setTimeout(() => window.open(naverMapWebUrl, "_blank", "noopener,noreferrer"), 800);
                                        } else {
                                            window.open(naverMapWebUrl, "_blank", "noopener,noreferrer");
                                        }
                                        
                                        // const threshold = await fetchPreferenceThreshold();
                                        // const preferenceType = determinePreferenceType(store, candidates, threshold);
                                        // await logUserSelection(store, goodId, preferenceType);
                                    }
                                }}
                            >
                                <img
                                    src="/navermap.png"
                                    style={{
                                        width: "100%",
                                        height: "100%",
                                        objectFit: "cover",
                                        borderRadius: "20%",
                                    }} />
                            </button>

                            {/* 카카오맵 */}
                            <button
                                {...touchEffect}
                                style={{
                                    flex: 1,
                                    aspectRatio: "1",
                                    borderRadius: "14px",
                                    background: "#fff",
                                    border: "none",
                                    boxShadow: "none",
                                    color: "#3A1D1D",
                                    fontSize: "15px",
                                    fontWeight: 600,
                                    display: "flex",
                                    justifyContent: "center",
                                    alignItems: "center",
                                }}
                                onClick={async () => {
                                    if (store.lat && store.lng) {
                                        const slat = pos.lat;
                                        const slng = pos.lng;
                                        const sname = encodeURIComponent("내 위치");
                                        const dlat = store.lat;
                                        const dlng = store.lng;
                                        const dname = encodeURIComponent(store.storeName);
                                        const kakaoMapUrl = `https://map.kakao.com/link/from/${sname},${slat},${slng}/to/${dname},${dlat},${dlng}`;
                                        // const threshold = await fetchPreferenceThreshold();
                                        // const preferenceType = determinePreferenceType(store, candidates, threshold);
                                        // await logUserSelection(store, goodId, preferenceType);
                                        window.open(kakaoMapUrl, "_blank");
                                    }
                                }}
                            >
                                <img
                                    src="/kakaomap.png"
                                    style={{
                                        width: "100%",
                                        height: "100%",
                                        objectFit: "cover",
                                        borderRadius: "20%",
                                    }} />
                            </button>

                        </div>
                        {/* 닫기 */}
                        <button
                            {...touchEffect}
                            style={{
                                marginTop: "20px",
                                width: "100%",
                                padding: "14px",
                                borderRadius: "14px",
                                background: "#f5f5f5",
                                border: "none",
                                color: "#333",
                                fontSize: "16px",
                                fontWeight: 500,
                            }}
                            onClick={() => setIsRouteModalOpen(false)}
                        >
                            닫기
                        </button>
                    </div>
                </div>
            )}
        </>
    );
}

export default StoreDetailPanel;