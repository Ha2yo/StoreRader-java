import { useMemo } from "react";

type Props = {
    page: number;          // 0-based
    size: number;          // page size
    totalCount: number;    // 전체 row 수
    onPageChange: (nextPage: number) => void;

    groupSize?: number;    // 기본 10
    className?: string;
};

export default function Pagination({
    page,
    size,
    totalCount,
    onPageChange,
    groupSize = 10,
}: Props) {
    const totalPages = Math.max(1, Math.ceil(totalCount / size));

    const { groupStart, groupEnd, pageIndexes } = useMemo(() => {
        const groupStart = Math.floor(page / groupSize) * groupSize; // 0-based
        const groupEnd = Math.min(groupStart + groupSize - 1, totalPages - 1);

        const pageIndexes = Array.from(
            { length: groupEnd - groupStart + 1 },
            (_, i) => groupStart + i
        );

        return { groupStart, groupEnd, pageIndexes };
    }, [page, groupSize, totalPages]);

    const canPrev = page > 0;
    const canNext = page < totalPages - 1;

    const go = (p: number) => {
        const clamped = Math.max(0, Math.min(totalPages - 1, p));
        onPageChange(clamped);
    };

    return (
        <div className="pagination">
            {/* 이전 그룹 */}
            <button disabled={groupStart === 0} onClick={() => go(groupStart - 1)}>
                {"<<"}
            </button>

            {/* 이전 페이지 */}
            <button disabled={!canPrev} onClick={() => go(page - 1)}>
                {"<"}
            </button>

            {pageIndexes.map((pi) => {
                const isCurrent = pi === page;
                return (
                    <button
                        key={pi}
                        onClick={() => go(pi)}
                        style={{
                            padding: "6px 10px",
                            borderRadius: 6,
                            border: "1px solid #ccc",
                            background: isCurrent ? "#222" : "transparent",
                            color: isCurrent ? "#fff" : "inherit",
                            cursor: "pointer",
                        }}
                    >
                        {pi + 1}
                    </button>
                );
            })}

            {/* 다음 페이지 */}
            <button disabled={!canNext} onClick={() => go(page + 1)}>
                {">"}
            </button>

            {/* 다음 그룹 */}
            <button disabled={groupEnd >= totalPages - 1} onClick={() => go(groupEnd + 1)}>
                {">>"}
            </button>
        </div>
    );
}
