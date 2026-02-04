import { addStoresTable } from "../../apis/add/addStoresTable";
import SseLog from "./sseLog";

function AddStores() {

     return (
    <div className="container">
      <div className="headerRow">
        <h1>Stores Table 동기화</h1>
      </div>
      <SseLog endpoint={addStoresTable.stores} />
    </div>
  );
}

export default AddStores;