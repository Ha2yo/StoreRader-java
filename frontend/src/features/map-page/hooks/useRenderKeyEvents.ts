import { useEffect, useState } from "react";

export function useRenderKeyEvent() {
    const [renderKey, setRenderKey] = useState(0);

    useEffect(() => {
        setRenderKey((prev) => prev + 1);

    }, []);
    return renderKey;
}