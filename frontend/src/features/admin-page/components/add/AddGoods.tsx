/**
 * File: features/admin-page/components/AddGoods.tsx
 * Description:
 *   관리자 페이지에서 상품 데이털르 동기화하기 위한 UI 컴포넌트
 */

import { addGoodsTable } from "../../apis/add/addGoodsTable";
import SseLog from "./sseLog";

function AddGoods() {

  return (
    <div className="container">
      <div className="headerRow">
        <h1>Goods Table 동기화</h1>
      </div>
      <SseLog endpoint={addGoodsTable.goods} />
    </div>
  );
}

export default AddGoods;