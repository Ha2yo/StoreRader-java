import { useNavigate } from "react-router-dom";
import Sidebar from "./SideBar";
import { useState } from "react";
import { useLastSearch } from "../hooks/useLastSearch";

function Search() {
  const navigate = useNavigate();
  const [isSidebarOpen, setIsSidebarOpen] = useState(false);

  const lastSearch = useLastSearch();

  return (
    <>
      <div
        className="search-bar-map"
        onClick={() => navigate("/search")}
      >
        {/* 햄버거 버튼 */}
        <button
          style={{
            all: "unset",
            fontSize: 25,
            width: 75,
            height: 50,
            cursor: "pointer",
            color: "#333",
            borderRadius: "25px 8px 8px 25px",
            //background: "rgba(0,0,0,0.05)",
          }}
          onClick={(e) => {
                e.stopPropagation();
                setIsSidebarOpen(true);
              }}
        >
          &nbsp;&nbsp;&nbsp;☰
        </button>

        <input
          className="search-bar-box"
          type="text"
          value={lastSearch}
          onClick={() => navigate("/search")}
          placeholder="상품명을 입력하세요"
          readOnly
        />
      </div>
      {isSidebarOpen && (
        <Sidebar onClose={() => setIsSidebarOpen(false)} />
      )}
    </>
  );
}

export default Search;