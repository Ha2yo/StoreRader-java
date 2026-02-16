import usePricesTable from "../../hooks/select/usePricesTable";
import { DataTable, type Column } from "./DataTable";
import type { Price, PriceSortKey } from "../../types/SelectPrices";
import Pagination from "./Pagination";

export default function SelectPrices() {
    const {
        rows,
        totalCount,
        isLoading,
        sortKey,
        sortOrder,
        handleSort,
        page,
        size,
        setPage
    } = usePricesTable();

    const columns: Column<Price, PriceSortKey>[] = [
        { key: "id", header: "ID", sortKey: "id", render: (p) => p.id },
        { key: "goodId", header: "상품 ID", sortKey: "goodId", render: (p) => p.goodId },
        { key: "storeId", header: "매장 ID", sortKey: "storeId", render: (p) => p.storeId },
        { key: "inspectDay", header: "조사일자", sortKey: "inspectDay", render: (p) => p.inspectDay },
        { key: "price", header: "가격", render: (p) => p.price },
        { key: "isOnePlusOne", header: "1+1 여부", render: (p) => p.isOnePlusOne },
        { key: "isDiscount", header: "할인 여부", render: (p) => p.isDiscount },
        { key: "discountStart", header: "할인 시작일", render: (p) => p.discountStart },
        { key: "discountEnd", header: "할인 종료일", render: (p) => p.discountEnd },
        {
            key: "createdAt",
            header: "생성일자",
            sortKey: "createdAt",
            render: (p) => new Date(p.createdAt).toLocaleDateString(),
        }
    ];

    return (
        <div>
            <DataTable<Price, PriceSortKey>
                title="PRices Table"
                totalCount={totalCount}
                isLoading={isLoading}
                rows={rows}
                columns={columns}
                sortKey={sortKey}
                sortOrder={sortOrder}
                onSort={handleSort}
                rowKey={(p) => p.id}
            />
            <Pagination
                page={page}
                size={size}
                totalCount={totalCount}
                onPageChange={setPage}
                groupSize={10} />
        </div>
    );
}
