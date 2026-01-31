import { touchEffect } from "../../../common/utils/touchEffect";
import { syncGoodsTable } from "../apis/syncGoodsTable";

function AddGoods () {
    return (
        <div className="container">
            <button
                    {...touchEffect}
                    style={{
                      width: "30%",
                      padding: "12px",
                      background: "#1890FF",
                      borderRadius: "10px",
                      color: "#FFFFFF",
                      fontSize: "16px",
                      fontWeight: "bold",
                      marginTop: "20px",
                    }}
                    onClick={() => syncGoodsTable()}
                  >
                    관리자 페이지
                  </button>
        </div>
    );
}

export default AddGoods;