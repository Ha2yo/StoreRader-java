/**
 * File: features/admin-page/components/Admin.tsx
 * Description:
 *   관리자 페이지의 메인 레이아웃 및 메뉴 제어를 담당하는 UI 컴포넌트
 */
import { useState } from "react";
import SelectUsers from "./select/SelectUsers";
import AddGoods from "./add/AddGoods";
import AddStores from "./add/AddStores";
import AddRegionCodes from "./add/AddRegionCodes";
import SelectStores from "./select/SelectStores";
import SelectGoods from "./select/SelectGoods";

function Admin() {
  const [activeMenu, setActiveMenu] = useState("dashboard");

  return (
    <div>
      <nav className="header">
        <h1
          style={{ cursor: "pointer" }}
          onClick={() => setActiveMenu("dashboard")}>
          StoreRader Admin
        </h1>
        <ul className="menu">
          <li
            className="dropdown">
            <span style={{ cursor: "pointer" }}>데이터 추가</span>

            <ul className="dropdown-menu">
              <li onClick={() => setActiveMenu("add_goods")}>goods</li>
              <li onClick={() => setActiveMenu("add_stores")}>stores</li>
              <li onClick={() => setActiveMenu("add_RegionCodes")}>regionCodes</li>
            </ul>
          </li>

          <li
            className="dropdown">
            <span style={{ cursor: "pointer" }}>DB 조회</span>

            <ul className="dropdown-menu">
              <li onClick={() => setActiveMenu("select_users")}>users</li>
              <li onClick={() => setActiveMenu("select_goods")}>goods</li>
              <li onClick={() => setActiveMenu("select_stores")}>stores</li>
            </ul>
          </li>
        </ul>
      </nav>

      <main className="container">
        {activeMenu === "dashboard" && <div>관리자 홈입니다.</div>}
        {activeMenu === "select_users" && <SelectUsers />}
        {activeMenu === "select_goods" && <SelectGoods />}
        {activeMenu === "select_stores" && <SelectStores />}
        {activeMenu === "add_goods" && <AddGoods />}
        {activeMenu === "add_stores" && <AddStores />}
        {activeMenu === "add_RegionCodes" && <AddRegionCodes />}
      </main>
    </div>
  );
}

export default Admin;