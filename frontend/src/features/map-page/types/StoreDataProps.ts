import type { Store } from "./StoreItem";

export interface StoreDataProps {
    map: L.Map | null;
    storeMarkersRef: React.RefObject<Record<string, L.Marker>>;
    circleRef: React.RefObject<L.Circle | null>;
    renderKey: number;
    setSelectedStore: (s: Store | null) => void;
}