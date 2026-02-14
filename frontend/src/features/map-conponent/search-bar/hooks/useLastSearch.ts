import { useEffect, useState } from "react";

export function useLastSearch() {
    const [lastSearch, setLastSearch] = useState("");

    useEffect(() => {
        const saved = localStorage.getItem("lastSearchTerm");
        if (saved) setLastSearch(saved);
    }, []);

    return lastSearch;
}