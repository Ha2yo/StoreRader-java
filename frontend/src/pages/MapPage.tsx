/**
 * File: pages/MapPage.tsx
 * Description:
 *   지도를 중심으로 한 주요 기능을 제공하는 페이지 컴포넌트
 */

import Map from "../features/map-page/components/Map";
import Search from "../features/search-bar/components/SearchBar";

function MapPage() {
  return (
    <div >
      <div className="container">
        <Search />
      </div>
      <Map />
    </div>
  );
}

export default MapPage;
