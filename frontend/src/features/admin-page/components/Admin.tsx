import { useState } from "react";
import UserTable from "./UserTable";

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
            style={{ cursor: "pointer" }}
            onClick={() => setActiveMenu("users")}>
            DB 조회 (users)
          </li>
        </ul>
      </nav>

      <main className="container">
        {activeMenu === "dashboard" && <div>관리자 대시보드입니다.</div>}
        {activeMenu === "users" && <UserTable />}
      </main>
    </div>
  );
}

export default Admin;