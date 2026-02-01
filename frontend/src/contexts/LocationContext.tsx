/**
 * File: contexts/LocationContext.tsx
 * Description:
 *   사용자의 현재 위치 정보를 추적하고 저장하기 위한 컨텍스트
 *
 * Responsibilities:
 *   1. 사용자 위치 조회
 *   2. 일정 주기로 위치 정보 갱신
 *   3. 마지막 위치 정보를 로컬 스토리지에 저장
 */


import { createContext, useEffect, useState } from "react";

export const LocationContext = createContext<GeolocationPosition | null>(null);

function LocationProvider({ children }: { children: React.ReactNode }) {

  const [position,] = useState<GeolocationPosition | null>(null);

  useEffect(() => {
    console.log("위치 추적 시작");

    if (!navigator.geolocation) {
      console.warn("이 기기는 위치 정보를 지원하지 않습니다.");
      return;
    }

    // 위치 정보 기록
    function updatePosition(position: GeolocationPosition) {
      var lat = position.coords.latitude
      var lng = position.coords.longitude
      var accuracy = position.coords.accuracy

      localStorage.setItem("lastPosition", JSON.stringify({ lat, lng, accuracy }));

      console.log("현재 좌표 -> lat: " + lat + " lng: " + lng + " accuracy: " + accuracy)
    }

    // 초기 위치 1회 조회
    navigator.geolocation.getCurrentPosition(
      updatePosition,
      function (err) {
        console.error("초기 위치 읽기 실패:", err);
      },
      { enableHighAccuracy: true }
    );

    // 5초 주기로 위치 정보 갱신
    const id = setInterval(() => {
      navigator.geolocation.getCurrentPosition(
        updatePosition,
        function (err) {
          console.error("주기적 위치 갱신 실패:", err);
        },
        { enableHighAccuracy: true }
      );
    }, 5000);

    return () => clearInterval(id);
  }, []);

  return (
    <LocationContext.Provider value={position}>
      {children}
    </LocationContext.Provider>
  );
}

export default LocationProvider;