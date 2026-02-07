import { useNavigate } from "react-router-dom";

function Search() {
  const navigate = useNavigate();

  return (
          <div
            className="search-bar-map"
            onClick={() => navigate("/search")}
          >
            {/* 햄버거 버튼 */}
            <button
              style={{
                all: "unset",
                fontSize: 25,
                width: 60,
                height: 50,
                cursor: "pointer",
                color: "#333",
                borderRadius: "25px 8px 8px 25px",
                //background: "rgba(0,0,0,0.05)",
              }}
            >
              &nbsp;&nbsp;☰
            </button>

            <input
              className="search-bar-box"
              type="text"
              onClick={() => navigate("/search")}
              placeholder="상품명을 입력하세요"
              readOnly
            />
          </div>
  );
}

export default Search;