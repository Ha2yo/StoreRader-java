export interface StoreDataProps {
    map: L.Map | null;
    storeMarkersRef: React.RefObject<Record<string, L.Marker>>;
    circleRef: React.RefObject<L.Circle | null>;
    renderKey: number;
}