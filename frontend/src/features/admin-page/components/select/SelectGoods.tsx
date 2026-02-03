import useGoodsTable from "../../hooks/useGoodsTable";

function SelectGoods() {
    const { goods, sortedGoods, isLoading, sortKey, sortOrder, handleSort } =
        useGoodsTable();

    if (isLoading)
        return <div>데이터를 불러오는 중입니다...</div>

    return (
        <div className="container">
            <div className="headerRow">
                <h1>Goods Table</h1>
                <span>총 {goods?.length || 0}개</span>
            </div>

            <div className="tableWrap">
                <table className="table">
                    <thead>
                        <tr>
                            {/* ID 정렬 */}
                            <th onClick={() => handleSort("id")} className="sortable">
                                ID {sortKey === "id" && (sortOrder === "asc" ? "▲" : "▼")}
                            </th>
                            <th onClick={() => handleSort("goodId")} className="sortable">
                                상품 ID {sortKey === "goodId" && (sortOrder === "asc" ? "▲" : "▼")}
                            </th>
                            <th>상품명</th>
                            <th>총 용량</th>
                            <th>분류 코드</th>

                            {/* 가입일 정렬 */}
                            <th onClick={() => handleSort("createdAt")} className="sortable">
                                생성일자 {sortKey === "createdAt" && (sortOrder === "asc" ? "▲" : "▼")}
                            </th>

                            {/* 최근 로그인 정렬 */}
                            <th onClick={() => handleSort("updatedAt")} className="sortable">
                                수정일자 {sortKey === "updatedAt" && (sortOrder === "asc" ? "▲" : "▼")}
                            </th>
                        </tr>
                    </thead>
                    <tbody>
                        {sortedGoods.map((good) => (
                            <tr key={good.id}>
                                <td>{good.id}</td>
                                <td>{good.goodId}</td>
                                <td>{good.goodName}</td>
                                <td>{good.totalCnt}</td>
                                <td>{good.totalDivCode}</td>
                                <td>
                                    {new Date(good.createdAt).toLocaleDateString()}
                                </td>
                                <td>
                                    {good.updatedAt ? new Date(good.updatedAt).toLocaleString() : "기록 없음"}
                                </td>
                            </tr>
                        ))}
                    </tbody>
                </table>
            </div>
        </div>
    );
}

export default SelectGoods;