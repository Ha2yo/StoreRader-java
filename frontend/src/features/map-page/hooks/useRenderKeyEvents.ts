import { useEffect, useState } from "react";

export function useRenderKeyEvent() {
    const [renderKey, setRenderKey] = useState(0);

    useEffect(() => {
        setRenderKey((prev) => prev + 1);

        const handleRegionChange = () => {
            setRenderKey((prev) => prev + 1);
        }

        const handleDistanceChange = () => {
            setRenderKey((prev) => prev + 1);
        }

        window.addEventListener("regionChange", handleRegionChange);
        window.addEventListener("distanceChange", handleDistanceChange);

        return () => {
            window.removeEventListener("regionChange", handleRegionChange);
            window.removeEventListener("distanceChange", handleDistanceChange);
        }
    }, []);
    return renderKey;
}