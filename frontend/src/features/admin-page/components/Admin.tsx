/**
 * File: features/admin-page/components/Admin.tsx
 * Description:
 *   관리자 페이지의 메인 레이아웃 및 메뉴 제어를 담당하는 UI 컴포넌트
 */
import { useState } from "react";
import SelectUsers from "./select/SelectUsers";
import SelectGoods from "./select/SelectGoods";
import AddGoods from "./add/AddGoods";


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
            </ul>
          </li>

          <li
            className="dropdown">
            <span style={{ cursor: "pointer" }}>DB 조회</span>

            <ul className="dropdown-menu">
              <li onClick={() => setActiveMenu("select_users")}>users</li>
              <li onClick={() => setActiveMenu("select_goods")}>goods</li>
            </ul>
          </li>
        </ul>
      </nav>

      <main className="container">
        {activeMenu === "dashboard" && <div>관리자 홈입니다.</div>}
        {activeMenu === "select_users" && <SelectUsers />}
        {activeMenu === "select_goods" && <SelectGoods />}
        {activeMenu === "add_goods" && <AddGoods />}
      </main>
    </div>
  );
}

export default Admin;