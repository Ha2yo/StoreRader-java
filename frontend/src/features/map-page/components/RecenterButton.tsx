import { type RefObject } from "react";
import { recenterMap } from "../utils/recenterMap";

type Props = {
  leafletMap: RefObject<L.Map | null>;
};

function RecenterButton({ leafletMap } : Props) {

    return (
        <div>
            <button
                onClick={recenterMap(leafletMap)}
                style={{
                    position: "absolute",
                    bottom: "150px",
                    right: "5%",
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
export default RecenterButton;