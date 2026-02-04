import { addRegionCodesTable } from "../../apis/add/addRegionCodesTable";
import SseLog from "./sseLog";

function AddRegionCodes() {

   return (
    <div className="container">
      <div className="headerRow">
        <h1>RegionCodes Table 동기화</h1>
      </div>
      <SseLog endpoint={addRegionCodesTable.regionCodes} />
    </div>
  );
}

export default AddRegionCodes;