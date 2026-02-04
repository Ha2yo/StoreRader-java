import React from "react";

type SortOrder = "asc" | "desc";

export type Column<T, K extends string> = {
    key: string;
    header: string;
    sortKey?: K;
    render: (row: T) => React.ReactNode;
};

type Props<T, K extends string> = {
    title: string;
    totalCount: number;
    isLoading: boolean;
    rows: T[];
    columns: Column<T, K>[];
    sortKey: K;
    sortOrder: SortOrder;
    onSort: (key: K) => void;
    rowKey: (row: T) => string | number;
};

export function DataTable<T, K extends string>({
    title,
    totalCount,
    isLoading,
    rows,
    columns,
    sortKey,
    sortOrder,
    onSort,
    rowKey,
}: Props<T, K>) {
    if (isLoading) return <div>데이터를 불러오는 중입니다...</div>;

    return (
        <div className="container">
            <div className="headerRow">
                <h1>{title}</h1>
                <span>총 {totalCount}개</span>
            </div>

            <div className="tableWrap">
                <table className="table">
                    <thead>
                        <tr>
                            {columns.map((col) => {
                                const sortable = Boolean(col.sortKey);
                                return (
                                    <th
                                        key={col.key}
                                        onClick={sortable ? () => onSort(col.sortKey!) : undefined}
                                        className={sortable ? "sortable" : undefined}
                                        style={sortable ? { cursor: "pointer" } : undefined}
                                    >
                                        {col.header}
                                        {sortable &&
                                            col.sortKey === sortKey &&
                                            (sortOrder === "asc" ? " ▲" : " ▼")}
                                    </th>
                                );
                            })}
                        </tr>
                    </thead>

                    <tbody>
                        {rows.map((row) => (
                            <tr key={rowKey(row)}>
                                {columns.map((col) => (
                                    <td key={col.key}>{col.render(row)}</td>
                                ))}
                            </tr>
                        ))}
                    </tbody>
                </table>
            </div>
        </div>
    );
}
