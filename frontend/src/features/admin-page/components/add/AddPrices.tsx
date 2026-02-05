import { useState } from "react";
import { addPricesTable } from "../../apis/add/addPricesTable";
import SseLog from "./SseLog";

function AddPrices() {
    const today = new Date().toISOString().slice(0, 10);

    const [inspectDay, setInspectDay] = useState(today);

    const formatDate = (value: string) => value.replaceAll("-", "");

    return (
        <div className="container">
            <div className="headerRow">
                <h1>Prices Table 동기화</h1>
            </div>

            <input
                type="date"
                value={inspectDay}
                onChange={(e) => setInspectDay(e.target.value)}
                style={{
                    width: "180px",
                    padding: "6px 8px",
                }}
            />

            <SseLog endpoint={addPricesTable.prices(formatDate(inspectDay))} />
        </div>
    );
}

export default AddPrices;