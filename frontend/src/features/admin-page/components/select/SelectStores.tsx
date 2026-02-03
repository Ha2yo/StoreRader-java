import useStoresTable from "../../hooks/useStoresTable";

function SelectStores() {
    const { stores, isLoading, sortKey, sortOrder, handleSort } =
        useStoresTable();

    if (isLoading)
        return <div>데이터를 불러오는 중입니다...</div>

    return (
        <div className="container">
            <div className="headerRow">
                <h1>Store Table</h1>
                <span>총 {stores.length}개</span>
            </div>

            <div className="tableWrap">
                <table className="table">
                    <thead>
                        <tr>
                            {/* ID 정렬 */}
                            <th onClick={() => handleSort("id")} className="sortable">
                                ID {sortKey === "id" && (sortOrder === "asc" ? "▲" : "▼")}
                            </th>
                            <th onClick={() => handleSort("storeId")} className="sortable">
                                매장 ID {sortKey === "storeId" && (sortOrder === "asc" ? "▲" : "▼")}
                            </th>
                            <th>전화번호</th>
                            <th>우편번호</th>
                            <th>지번주소</th>
                            <th>도로주소</th>
                            <th>위도</th>
                            <th>경도</th>
                            <th>지역코드</th>
                            <th>지역상세코드</th>

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
                        {/* {sortedStores.map((store) => (
                            <tr key={store.id}>
                                <td>{store.id}</td>
                                <td>{store.storeId}</td>
                                <td>{store.storeName}</td>
                                <td>{store.totalCnt}</td>
                                <td>{store.totalDivCode}</td>
                                <td>
                                    {new Date(store.createdAt).toLocaleDateString()}
                                </td>
                                <td>
                                    {store.updatedAt ? new Date(store.updatedAt).toLocaleString() : "기록 없음"}
                                </td>
                            </tr>
                        ))} */}
                    </tbody>
                </table>
            </div>
        </div>
    );
}

export default SelectStores;